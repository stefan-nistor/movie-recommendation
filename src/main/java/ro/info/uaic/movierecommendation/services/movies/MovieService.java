package ro.info.uaic.movierecommendation.services.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieLabelDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.Type;
import ro.info.uaic.movierecommendation.repositories.movies.ActorRepository;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;
import ro.info.uaic.movierecommendation.repositories.movies.MovieTypeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository repositoryMovie;

    @Autowired
    private ActorRepository repositoryActor;

    @Autowired
    private MovieTypeRepository repositoryType;

    @Autowired
    private ModelMapper mapper;

    public List<MovieDto> findAll(Pageable paging) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = repositoryMovie.findAll(paging).getContent().stream()
                .filter(movie -> !movie.isDeleted()).map(movie -> modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "without parameters", "");
        }
        return movieDtoList;
    }

    public List<MovieDto> findMostVoted(Pageable paging, int numberTop) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = repositoryMovie.findAll(paging).getContent().stream()
                .filter(movie -> !movie.isDeleted()).map(movie -> modelMapper.map(movie, MovieDto.class))
                .sorted((Comparator.comparingDouble(MovieDto::getVoteAverage).reversed())).limit(numberTop)
                .collect(Collectors.toList());

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "without parameters", "");
        }
        return movieDtoList;
    }

    public MovieDto findByName(String name, Pageable paging) throws MovieNotFoundException {

        Optional<Movie> movie = repositoryMovie.findByName(name);

        if (movie.isEmpty() || movie.get().isDeleted()) {
            throw new MovieNotFoundException(Movie.class, "name", name);
        }

        return mapper.map(movie, MovieDto.class);
    }

    public List<MovieDto> findByType(List<Type> type, Pageable paging) {

        List<MovieDto> movieDtoList = repositoryMovie.findByTypeIn(repositoryType.findByTypeIn(type), paging)
                .getContent().stream().filter(movie -> !movie.isDeleted())
                .map(movie -> modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "type/types", type.toString());
        }
        return movieDtoList;
    }

    public List<MovieDto> findByActor(List<String> actorName, Pageable paging) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = repositoryMovie.findByActorsIn(repositoryActor.findByNameIn(actorName), paging)
                .getContent().stream().filter(movie -> !movie.isDeleted())
                .map(movie -> modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "actor/actors", actorName.toString());
        }
        return movieDtoList;

    }

    public Optional<Movie> getMovieById(Long id) {
        if (repositoryMovie.existsById(id)) {
            if (!repositoryMovie.getById(id).isDeleted()) {
                return repositoryMovie.findById(id);
            }
        }
        return repositoryMovie.findById(0L);

    }

    public MovieDto createMovie(MovieDto newMovie) {

        Movie insertedMovie = mapper.map(newMovie, Movie.class);
        insertedMovie.setDeleted(false);
        repositoryMovie.save(insertedMovie);

        return newMovie;
    }

    public boolean deleteMovie(Movie foundMovie) {

        if (!repositoryMovie.existsById(foundMovie.getId())) return false;

        foundMovie.setDeleted(true);
        repositoryMovie.save(foundMovie);
        return true;

    }

    public MovieDto updateMovie(Movie movie) {

        if (!repositoryMovie.existsById(movie.getId())) throw new MovieNotFoundException(Movie.class, "id");

        if (movie.isDeleted()) throw new MovieNotFoundException(Movie.class, "id");

        repositoryMovie.save(movie);
        MovieDto updatedMovie = mapper.map(movie, MovieDto.class);
        return updatedMovie;
    }


    public Boolean getSinglePrediction(UserMovieLabelDto userMovieLabelDto) {
        JSONObject jsonObject = new JSONObject();
      
        jsonObject.put("userId", userMovieLabelDto.getUserId());
        jsonObject.put("movieId", userMovieLabelDto.getMovieId());
        jsonObject.put("label", userMovieLabelDto.getLabel());

        String URL = "https://mlmoduleapi.azurewebsites.net/Recommendations/singlePrediction";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

        JSONObject resultJson = restTemplate.postForObject(URL, request, JSONObject.class);
        if (resultJson == null) {
            throw new RuntimeException("No result");
        }

        return (Boolean) resultJson.get("predictionLabel");
    }

    public List<MovieDto> getPredictions(Integer noOfPredictions, List<UserMovieLabelDto> userMovieLabelList) {
        JSONArray jsonArray = new JSONArray(userMovieLabelList);
        List<MovieDto> moviesToReturn = new ArrayList<>();

        String URL = "https://mlmoduleapi.azurewebsites.net/Recommendations/predictions/" + noOfPredictions;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonArray.toString(), headers);

        Object[] resultJson = restTemplate.postForObject(URL, request, Object[].class);

        if (resultJson == null) {
            throw new RuntimeException("No result");
        }

        for (Object object : resultJson) {
            Long currMovieId = Long.parseLong(object.toString());
            Optional<Movie> currMovieOpt = repositoryMovie.findById(currMovieId);

            if (currMovieOpt.isPresent()) {
                MovieDto currMovieDto = mapper.map(currMovieOpt.get(), MovieDto.class);
                moviesToReturn.add(currMovieDto);
            }
        }

        return moviesToReturn;
    }
}

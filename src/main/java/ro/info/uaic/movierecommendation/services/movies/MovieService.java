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
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieRatingDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
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
    private MovieRepository movieRepo;

    @Autowired
    private ActorRepository actorRepo;

    @Autowired
    private MovieTypeRepository typeRepo;

    @Autowired
    private ModelMapper mapper;

    public Map<String, Object> findAll(Pageable paging) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = movieRepo.findAll(paging).getContent().stream()
                .filter(movie -> !movie.isDeleted()).map(movie -> modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "without parameters", "");
        }

        Map<String, Object> movieList = new HashMap<>();
        movieList.put("count", movieRepo.countByIsDeleted(false));
        movieList.put("movies", movieDtoList);
        return movieList;
    }

    public List<MovieDto> findMostVoted(Pageable paging, int numberTop) throws MovieNotFoundException {
        List<MovieDto> movieDtoList = movieRepo.findAll(paging).getContent().stream()
                .filter(movie -> !movie.isDeleted()).map(movie -> modelMapper.map(movie, MovieDto.class))
                .sorted((Comparator.comparingDouble(MovieDto::getVoteAverage).reversed())).limit(numberTop)
                .collect(Collectors.toList());

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "without parameters", "");
        }
        return movieDtoList;
    }

    public MovieDto findByName(String name, Pageable paging) throws MovieNotFoundException {

        Optional<Movie> movie = movieRepo.findByName(name);

        if (movie.isEmpty() || movie.get().isDeleted()) {
            throw new MovieNotFoundException(Movie.class, "name", name);
        }

        return mapper.map(movie, MovieDto.class);
    }

    public Map<String, Object> findByType(List<Type> type, Pageable paging) {
        List<MovieDto> movieDtoList = movieRepo.findByTypeIn(typeRepo.findByTypeIn(type), paging)
                .getContent().stream().filter(movie -> !movie.isDeleted())
                .map(movie -> modelMapper.map(movie, MovieDto.class)).toList();

        List<MovieType> movieTypes = new ArrayList<>();
        type.forEach(t -> movieTypes.add(typeRepo.findByType(t)));

        List<MovieDto> movieDtoListAll= new ArrayList<>();
        movieDtoList.forEach(movieDto -> {
            if (movieDto.getType().containsAll(movieTypes)) {
                movieDtoListAll.add(movieDto);
            }
        });

        if (movieDtoListAll.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "type/types", type.toString());
        }

        List<Movie> tempList = movieRepo.findByTypeIn(typeRepo.findByTypeIn(type));
        long count = 0L;
        int i = 0;
        while (i < tempList.size()) {
            if (tempList.get(i).isDeleted() || tempList.get(i).getDescription() == null) {
                tempList.remove(tempList.get(i));
                i--;
            } else {
                count++;
            }
            i++;
        }

        Map<String, Object> movieList = new HashMap<>();
        movieList.put("count", count);
        movieList.put("movies", movieDtoList);

        return movieList;
    }

    public List<MovieDto> findByActor(List<String> actorName, Pageable paging) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = movieRepo.findByActorsIn(actorRepo.findByNameIn(actorName), paging)
                .getContent().stream().filter(movie -> !movie.isDeleted())
                .map(movie -> modelMapper.map(movie, MovieDto.class)).toList();

        if (movieDtoList.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "actor/actors", actorName.toString());
        }

        return movieDtoList;
    }

    public MovieDto getMovieById(Long id) {
        Optional<Movie> movie = movieRepo.findById(id);
        if (movie.isEmpty() || movie.get().isDeleted()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }

        return mapper.map(movie.get(), MovieDto.class);
    }

    public MovieDto createMovie(MovieDto newMovie) {

        Movie insertedMovie = mapper.map(newMovie, Movie.class);
        insertedMovie.setDeleted(false);
        movieRepo.save(insertedMovie);

        return newMovie;
    }

    public boolean deleteMovie(Long movieId) {
        Optional<Movie> movie = movieRepo.findById(movieId);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }

        movie.get().setDeleted(true);
        movieRepo.save(movie.get());
        return true;

    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Optional<Movie> movie = movieRepo.findById(id);
        if (movie.isEmpty() || movie.get().isDeleted()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }

        if (movieDto.getName() != null) {
            movie.get().setName(movieDto.getName());
        }
        if (movieDto.getDescription() != null) {
            movie.get().setDescription(movieDto.getDescription());
        }
        if (movieDto.getType() != null) {
            movie.get().setType(movieDto.getType());
        }
        if (movieDto.getDuration() != null) {
            movie.get().setDuration(movieDto.getDuration());
        }
        if (movieDto.getReleaseDate() != null) {
            movie.get().setReleaseDate(movieDto.getReleaseDate());
        }

        movieRepo.save(movie.get());
        return mapper.map(movie.get(), MovieDto.class);
    }


    public Boolean getSinglePrediction(UserMovieRatingDto userMovieRatingDto) {
        UserMovieLabelDto userMovieLabelDto = mapper.map(userMovieRatingDto, UserMovieLabelDto.class);
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

    public Map<String, Object> getPredictions(Integer noOfPredictions, List<UserMovieRatingDto> userMovieRatingDtoList) {
        List<UserMovieLabelDto> userMovieLabelList = userMovieRatingDtoList.stream()
                .map(rating -> mapper.map(rating, UserMovieLabelDto.class)).toList();
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
            Optional<Movie> currMovieOpt = movieRepo.findById(currMovieId);

            if (currMovieOpt.isPresent() && !currMovieOpt.get().isDeleted()) {
                MovieDto currMovieDto = mapper.map(currMovieOpt.get(), MovieDto.class);
                moviesToReturn.add(currMovieDto);
            }
        }

        long count = 0L;
        int i = 0;
        while (i < moviesToReturn.size()) {
            if (moviesToReturn.get(i).isDeleted() || moviesToReturn.get(i).getDescription() == null) {
                moviesToReturn.remove(moviesToReturn.get(i));
                i--;
            } else {
                count++;
            }
            i++;
        }

        Map<String, Object> movieList = new HashMap<>();
        movieList.put("count", count);
        movieList.put("movies", moviesToReturn);

        return movieList;
    }

    public MovieDto findById(Long movieId) {
        Optional<Movie> movie = movieRepo.findById(movieId);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }

        return mapper.map(movie.get(), MovieDto.class);
    }
}

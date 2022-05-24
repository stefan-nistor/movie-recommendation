package ro.info.uaic.movierecommendation.services.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieListDTO;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.*;
import ro.info.uaic.movierecommendation.models.movies.MovieList;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.repositories.movies.MovieListRepository;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieListService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository repositoryMovie;

    @Autowired
    private MovieListRepository repositoryList;

    @Autowired
    private UserRepo userRepo;

    public List<MovieListDTO> findAll()  {

        List<MovieListDTO> movieListDTOList = repositoryList.findAll().stream()
                .map(movieList -> modelMapper.map(movieList, MovieListDTO.class))
                .collect(Collectors.toList());
        if (movieListDTOList.isEmpty()) {
            throw new MovieListNotFoundException(MovieList.class, "without parameters");
        }

        return movieListDTOList;
    }

    public MovieListDTO findByName(String name) {
        MovieListDTO movieListDTO = modelMapper.map(repositoryList.findByName(name), MovieListDTO.class);
        if (movieListDTO == null) {
            throw new MovieListNotFoundException(MovieList.class, "name", name);
        }
        return movieListDTO;
    }

    public MovieListDTO findByID(Long movieListID) {
        MovieListDTO movieListDTO = modelMapper.map(repositoryList.findById(movieListID), MovieListDTO.class);
        return movieListDTO;
    }

    public MovieListDTO addMovieToList(MovieDto movieDTO, MovieListDTO movieListDTO) {
        MovieList movieList = modelMapper.map(repositoryList.findByUserAndName(movieListDTO.getUser(),movieListDTO.getName()),
                MovieList.class);
        Movie movie = modelMapper
                .map((repositoryMovie.findByName(movieDTO.getName(), Pageable.unpaged()))
                        .getContent().get(0), Movie.class);
        if (movieList.getMovies().contains(movie)) {
            throw new MovieAlreadyInListException(MovieList.class, "Movie Name", movie.getName(),
                    "MovieList Name", movieList.getName());
        }
        movieList.getMovies().add(movie);
        MovieListDTO movieListDTOResult = modelMapper
                .map(repositoryList.findById(movieList.getId()), MovieListDTO.class);
        repositoryList.save(movieList);
        return movieListDTOResult;
    }

    public MovieListDTO removeMovieToList(MovieDto movieDTO, MovieListDTO movieListDTO) {
        MovieList movieList = modelMapper.map(repositoryList.findByUserAndName(movieListDTO.getUser(), movieListDTO.getName()),
                MovieList.class);
        Movie movie = modelMapper
                .map((repositoryMovie.findByName(movieDTO.getName(), Pageable.unpaged()))
                        .getContent().get(0), Movie.class);
        if (!movieList.getMovies().contains(movie)) {
            throw new MovieNotInListException(MovieList.class, "Movie Name", movie.getName(),
                    "MovieList Name", movieList.getName());
        }
        movieList.getMovies().remove(movie);
        MovieListDTO movieListDTOResult = modelMapper
                .map(repositoryList.findById(movieList.getId()), MovieListDTO.class);
        repositoryList.save(movieList);
        return movieListDTOResult;
    }

    public MovieListDTO create(String movieListName, Long userId) throws UserNotFoundException {
        if(!userRepo.existsById(userId)){
            throw new UserNotFoundException("No user for this id");
        }
        if (repositoryList.findByName(movieListName).isPresent()) {
            throw new MovieListAlreadyExistsException(MovieList.class, "MovieList Name", movieListName);
        }
        MovieListDTO movieListDTO = new MovieListDTO();
        movieListDTO.setName(movieListName);
        movieListDTO.setUser(modelMapper.map(userRepo.findById(userId),UserEntity.class));
        repositoryList.save(modelMapper.map(movieListDTO, MovieList.class));
        return movieListDTO;
    }

    public void delete(MovieListDTO movieListDTO) {
        Optional<MovieList> movieList = repositoryList.findByUserAndName(movieListDTO.getUser(), movieListDTO.getName());
        if(movieList.isEmpty()){
            throw new MovieListNotFoundException(MovieList.class,"user + name");
        }
        repositoryList.delete(movieList.get());
    }

    public List<MovieListDTO> findUserLists(Long userId) {
        List<MovieListDTO> movieListDTOList = repositoryList.findByUser(modelMapper.map(userRepo.findById(userId),UserEntity.class)).stream()
                .map(movieList -> modelMapper.map(movieList, MovieListDTO.class))
                .collect(Collectors.toList());
        return movieListDTOList;
    }

    public MovieListDTO findListOfUser(Long userId, String name) throws UserNotFoundException {
        Optional<UserEntity> user = userRepo.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException("No user for this id");
        }
        Optional<MovieList> movieList = repositoryList.findByUserAndName(user.get(),name);
        if(movieList.isEmpty()){
            throw new MovieListNotFoundException(MovieList.class,"user + name");
        }
        return modelMapper.map(movieList.get(), MovieListDTO.class);
    }

}

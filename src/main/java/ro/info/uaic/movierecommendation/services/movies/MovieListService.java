package ro.info.uaic.movierecommendation.services.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieListDTO;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieAlreadyInListException;
import ro.info.uaic.movierecommendation.exceptions.MovieListAlreadyExistsException;
import ro.info.uaic.movierecommendation.exceptions.MovieListNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.MovieNotInListException;
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

    public List<MovieListDTO> findAll() {
        List<MovieListDTO> movieListDTOList = repositoryList.findAll().stream()
                .map(user -> modelMapper.map(user, MovieListDTO.class))
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
        MovieList movieList = modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class);
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
        MovieList movieList = modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class);
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

    public MovieListDTO create(String movieListName, Long userID) {
        if (repositoryList.findByName(movieListName).isPresent()) {
            throw new MovieListAlreadyExistsException(MovieList.class, "MovieList Name", movieListName);
        }
        MovieListDTO movieListDTO = new MovieListDTO();
        movieListDTO.setName(movieListName);
        movieListDTO.setUser(userRepo.getById(userID));
        repositoryList.save(modelMapper.map(movieListDTO, MovieList.class));
        return movieListDTO;
    }

    public void delete(MovieListDTO movieListDTO) {
        MovieList movieList = modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class);
        System.out.println(movieList);
        repositoryList.delete(movieList);
    }

    public List<MovieListDTO> findUserLists(Long userID) {
        List<MovieListDTO> movieListDTOList = repositoryList.findByUser(userRepo.getById(userID)).stream()
                .map(user -> modelMapper.map(user, MovieListDTO.class))
                .collect(Collectors.toList());
        return movieListDTOList;
    }

    public List<MovieDto> findUserListMovies(Long userID, String name) {
        MovieList movieList = modelMapper.map(repositoryList.
                findByUserAndName(userRepo.getById(userID), name), MovieList.class);
        List<MovieDto> movieDtos = movieList.getMovies().stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtos;

    }

}

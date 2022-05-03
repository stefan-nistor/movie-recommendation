package ro.info.uaic.movierecommendation.services.lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ro.info.uaic.movierecommendation.dtoresponses.lists.MovieListDTO;
import ro.info.uaic.movierecommendation.models.lists.MovieList;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.reposirories.lists.MovieListRepository;

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

    public List<MovieListDTO> findAll() {
        List<MovieListDTO> movieListDTOList = repositoryList.findAll()
                .stream().map(user -> modelMapper.map(user, MovieListDTO.class))
                .collect(Collectors.toList());
        return movieListDTOList;
    }

    public MovieListDTO findByName(String name) {
        MovieListDTO movieListDTO = modelMapper.map(repositoryList.findByName(name), MovieListDTO.class);
        return movieListDTO;
    }

    public MovieListDTO findByID(long movieListID) {
        MovieListDTO movieListDTO = modelMapper.map(repositoryList.findByID(movieListID), MovieListDTO.class);
        return movieListDTO;
    }

    public MovieListDTO addMovieToList(MovieDto movie, MovieListDTO movieListDTO) {
        long movieListID = modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class).getId();
        long movieID = modelMapper.map(repositoryMovie.findByName(movie.getName()), Movie.class).getId();
        repositoryList.addMovieToList(movieListID, movieID);
        MovieListDTO movieListDTOResult = modelMapper.map(repositoryList.findByID(movieListID), MovieListDTO.class);
        return movieListDTOResult;
    }

    public MovieListDTO removeMovieToList(MovieDto movie, MovieListDTO movieListDTO) {
        long movieListID = modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class).getId();
        long movieID = modelMapper.map(repositoryMovie.findByName(movie.getName()), Movie.class).getId();
        repositoryList.removeMovieFromList(movieListID, movieID);
        MovieListDTO movieListDTOResult = modelMapper.map(repositoryList.findByID(movieListID), MovieListDTO.class);
        return movieListDTOResult;
    }

}

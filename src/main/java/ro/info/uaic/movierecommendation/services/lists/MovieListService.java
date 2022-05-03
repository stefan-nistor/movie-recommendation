package ro.info.uaic.movierecommendation.services.lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ro.info.uaic.movierecommendation.dtoresponses.lists.MovieListDTO;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.reposirories.lists.MovieListRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieListService {

    @Autowired
    private ModelMapper modelMapper;

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

    public MovieListDTO addMovieToList(Movie movie, long movieListID) {
        repositoryList.addMovieToList(movie, movieListID);
        return findByID(movieListID);
    }

    public MovieListDTO removeMovieToList(Movie movie, long movieListID) {
        repositoryList.removeMovieFromList(movie, movieListID);
        return findByID(movieListID);
    }

}

package ro.info.uaic.movierecommendation.services.lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ro.info.uaic.movierecommendation.dtoresponses.lists.MovieListDTO;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.lists.MovieList;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.repositories.lists.MovieListRepository;
import ro.info.uaic.movierecommendation.repositories.lists.MovieListRepository;
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

    public MovieListDTO findByID(Long movieListID) {
        MovieListDTO movieListDTO = modelMapper.map(repositoryList.findByid(movieListID), MovieListDTO.class);
        return movieListDTO;
    }

    public MovieListDTO addMovieToList(MovieDto movie, MovieListDTO movieListDTO) {
        MovieList movieList=modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class);
        Movie movie1=modelMapper.map(repositoryMovie.findByName(movie.getName(), Pageable.unpaged()), Movie.class);
        movieList.getMovies().add(movie1);
        MovieListDTO movieListDTOResult = modelMapper.map(repositoryList.findByid(movieList.getId()), MovieListDTO.class);
        return movieListDTOResult;
    }

    public MovieListDTO removeMovieToList(MovieDto movie, MovieListDTO movieListDTO) {
        MovieList movieList=modelMapper.map(repositoryList.findByName(movieListDTO.getName()), MovieList.class);
        Movie movie1=modelMapper.map(repositoryMovie.findByName(movie.getName(), Pageable.unpaged()), Movie.class);
        movieList.getMovies().remove(movie1);
        MovieListDTO movieListDTOResult = modelMapper.map(repositoryList.findByid(movieList.getId()), MovieListDTO.class);
        return movieListDTOResult;
    }

}

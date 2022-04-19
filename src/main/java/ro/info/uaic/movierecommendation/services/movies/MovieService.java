package ro.info.uaic.movierecommendation.services.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.repositories.movies.ActorRepository;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;

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


    public List<MovieDto> findAll() {

        List<MovieDto> movieDtoList = repositoryMovie.findAll()
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtoList;
    }

    public MovieDto findByName(String name) {

        MovieDto movieResponse = modelMapper.map(repositoryMovie.findByName(name), MovieDto.class);
        return movieResponse;
    }

    public MovieDto findByType(MovieType type) {

        MovieDto movieResponse = modelMapper.map(repositoryMovie.findByType(type), MovieDto.class);
        return movieResponse;
    }

    public MovieDto findByActor(String actorName) {

        MovieDto movieResponse = modelMapper.map(repositoryMovie.findByActors(repositoryActor.findByName(actorName)), MovieDto.class);
        return movieResponse;
    }


}

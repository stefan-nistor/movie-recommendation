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

    public List<MovieDto> findByName(String name) {

        List<MovieDto> movieDtoList = repositoryMovie.findByName(name)
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtoList;
    }

    public List<MovieDto> findByType(MovieType type) {

        List<MovieDto> movieDtoList = repositoryMovie.findByType(type)
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtoList;
    }

    public List<MovieDto> findByActor(String actorName) {

        List<MovieDto> movieDtoList = repositoryMovie.findByActors(repositoryActor.findByName(actorName))
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtoList;

    }


}

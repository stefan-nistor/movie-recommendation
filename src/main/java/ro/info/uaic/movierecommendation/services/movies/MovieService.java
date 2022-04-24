package ro.info.uaic.movierecommendation.services.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
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
    private MovieRepository repositoryMovie;

    @Autowired
    private ActorRepository repositoryActor;

    @Autowired
    private MovieTypeRepository repositoryType;

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

    public List<MovieDto> findByType(List<Type> type) {

        List<MovieDto> movieDtoList = repositoryMovie.findByTypeIn(repositoryType.findByTypeIn(type))
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtoList;
    }

    public List<MovieDto> findByActor(List<String> actorName) {

        List<MovieDto> movieDtoList = repositoryMovie.findByActorsIn(repositoryActor.findByNameIn(actorName))
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        return movieDtoList;

    }


}

package ro.info.uaic.movierecommendation.services.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public List<MovieDto> findAll(Pageable paging) throws MovieNotFoundException{

        List<MovieDto> movieDtoList = repositoryMovie.findAll(paging).getContent()
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());

        if(movieDtoList.isEmpty()){
            throw new MovieNotFoundException(Movie.class, "without parameters");
        }
        return movieDtoList;
    }

    public List<MovieDto> findByName(String name, Pageable paging) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = repositoryMovie.findByName(name, paging).getContent()
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());

        if(movieDtoList.isEmpty()){
            throw new MovieNotFoundException(Movie.class, "name", name);
        }
        return movieDtoList;
    }

    public List<MovieDto> findByType(List<Type> type, Pageable paging) {

        List<MovieDto> movieDtoList = repositoryMovie.findByTypeIn(repositoryType.findByTypeIn(type), paging).getContent()
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        if(movieDtoList.isEmpty()){
            throw new MovieNotFoundException(Movie.class, "actor/actors", type.toString());
        }
        return movieDtoList;
    }

    public List<MovieDto> findByActor(List<String> actorName, Pageable paging) throws MovieNotFoundException {

        List<MovieDto> movieDtoList = repositoryMovie.findByActorsIn(repositoryActor.findByNameIn(actorName), paging).getContent()
                .stream()
                .map(user -> modelMapper.map(user, MovieDto.class))
                .collect(Collectors.toList());
        if(movieDtoList.isEmpty()){
            throw new MovieNotFoundException(Movie.class, "actor/actors", actorName.toString());
        }
        return movieDtoList;

    }


}

package ro.info.uaic.movierecommendation.services.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import ro.info.uaic.movierecommendation.dtoresponses.UserObj;
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
    static final String USER_NOT_FOUND_FOR_NAME = "User not found for name.";
    static final String MOVIELIST_NAME = "MovieList Name";
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private MovieListRepository listRepo;

    @Autowired
    private UserRepo userRepo;


    public List<MovieListDTO> findAll() {

        List<MovieListDTO> movieListDTOList = listRepo.findAll().stream()
                .map(movieList -> modelMapper.map(movieList, MovieListDTO.class))
                .collect(Collectors.toList());
        if (movieListDTOList.isEmpty()) {
            throw new MovieListNotFoundException(MovieList.class, "without parameters");
        }

        return movieListDTOList;
    }


    public MovieListDTO findByName(String name) {
        MovieListDTO movieListDTO = modelMapper.map(listRepo.findByName(name), MovieListDTO.class);
        if (movieListDTO == null) {
            throw new MovieListNotFoundException(MovieList.class, "name", name);
        }
        return movieListDTO;
    }

    public MovieListDTO findByID(Long movieListID) {
        MovieListDTO movieListDTO = modelMapper.map(listRepo.findById(movieListID), MovieListDTO.class);
        return movieListDTO;
    }

    public MovieListDTO addMovieToList(MovieDto movieDTO, MovieListDTO movieListDTO) {

        if (movieListDTO == null) {
            throw new MovieListNotFoundException(MovieList.class, "without parameters");
        }
        Optional<UserEntity> user = userRepo.findByUsername(movieListDTO.getUser().getUsername());
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_FOR_NAME);
        }

        MovieList movieList = modelMapper.map(listRepo.findByUserAndName(user.get(), movieListDTO.getName()),
                MovieList.class);
        Movie movie = modelMapper
                .map((movieRepo.findByName(movieDTO.getName(), Pageable.unpaged()))
                        .getContent().get(0), Movie.class);
        if (movieList.getMovies().contains(movie)) {
            throw new MovieAlreadyInListException(MovieList.class, "Movie Name", movie.getName(),
                    MOVIELIST_NAME, movieList.getName());
        }

        movieList.getMovies().add(movie);
        MovieListDTO movieListDTOResult = modelMapper
                .map(listRepo.findById(movieList.getId()), MovieListDTO.class);
        listRepo.save(movieList);
        return movieListDTOResult;
    }

    public MovieListDTO removeMovieToList(MovieDto movieDTO, MovieListDTO movieListDTO) {
        Optional<UserEntity> user = userRepo.findByUsername(movieListDTO.getUser().getUsername());
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_FOR_NAME);
        }

        MovieList movieList = modelMapper.map(listRepo.findByUserAndName(user.get(), movieListDTO.getName()),
                MovieList.class);
        Movie movie = modelMapper
                .map((movieRepo.findByName(movieDTO.getName(), Pageable.unpaged()))
                        .getContent().get(0), Movie.class);
        if (!movieList.getMovies().contains(movie)) {
            throw new MovieNotInListException(MovieList.class, "Movie Name", movie.getName(),
                    MOVIELIST_NAME, movieList.getName());
        }
        movieList.getMovies().remove(movie);
        MovieListDTO movieListDTOResult = modelMapper
                .map(listRepo.findById(movieList.getId()), MovieListDTO.class);
        listRepo.save(movieList);
        return movieListDTOResult;
    }

    public MovieListDTO create(String movieListName, Long userId) throws UserNotFoundException {
        Optional<UserEntity> user = userRepo.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("No user for this id");
        }
        if (listRepo.findByName(movieListName).isPresent()) {
            throw new MovieListAlreadyExistsException(MovieList.class, MOVIELIST_NAME, movieListName);
        }
        MovieList movieList = new MovieList();
        movieList.setName(movieListName);
        movieList.setUser(user.get());
        listRepo.save(movieList);

        MovieListDTO movieListDTO = new MovieListDTO();
        UserObj userObj = modelMapper.map(movieList.getUser(), UserObj.class);
        movieListDTO.setMovies(movieList.getMovies());
        movieListDTO.setUser(userObj);
        movieListDTO.setName(movieList.getName());
        return movieListDTO;
    }

    public void delete(MovieListDTO movieListDTO) {
        Optional<UserEntity> user = userRepo.findByUsername(movieListDTO.getUser().getUsername());
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_FOR_NAME);
        }

        Optional<MovieList> movieList = listRepo.findByUserAndName(user.get(), movieListDTO.getName());
        if (movieList.isEmpty()) {
            throw new MovieListNotFoundException(MovieList.class, "user + name");
        }
        listRepo.delete(movieList.get());
    }

    public List<MovieListDTO> findUserLists(Long userId) {
        Optional<UserEntity> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_FOR_NAME);
        }

        return listRepo.findByUser(user.get()).stream()
                .map(movieList -> modelMapper.map(movieList, MovieListDTO.class))
                .collect(Collectors.toList());
    }

    public MovieListDTO findListOfUser(Long userId, String name) throws UserNotFoundException {
        Optional<UserEntity> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user for this id");
        }

        Optional<MovieList> movieList = listRepo.findByUserAndName(user.get(), name);
        if (movieList.isEmpty()) {
            throw new MovieListNotFoundException(MovieList.class, "user + name");
        }
        return modelMapper.map(movieList.get(), MovieListDTO.class);
    }

}

package ro.info.uaic.movierecommendation.controllers.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieListDTO;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.services.movies.MovieListService;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movielists")
public class MovieListController {

    @Autowired
    private MovieListService movieListService;

    @Autowired
    private MovieService movieService;


    @GetMapping
    public ResponseEntity<List<MovieListDTO>> getMovieLists() {
        return ResponseEntity.ok().body(movieListService.findAll());
    }


    @GetMapping("/names")
    public ResponseEntity<MovieListDTO> getMovieListByName(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(movieListService.findByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<MovieListDTO> addMovieToList(@RequestParam("userId") Long userId,
                                                       @RequestParam("lname") String movieListName,
                                                       @RequestParam("movieId") Long movieId) throws UserNotFoundException {
        return ResponseEntity.ok().body(
                movieListService.addMovieToList(movieService.findById(movieId),
                        movieListService.findListOfUser(userId, movieListName)));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<MovieListDTO> removeMovieFromList(@RequestParam("userId") Long user_id,
                                                            @RequestParam("lname") String movieListName,
                                                            @RequestParam("movieId") Long movieId) throws UserNotFoundException {
        return ResponseEntity.ok().body(
                movieListService.removeMovieToList(movieService.findById(movieId),
                        movieListService.findListOfUser(user_id, movieListName)));
    }

    @PostMapping
    public ResponseEntity<MovieListDTO> createList(@RequestParam("userId") Long userId,
                                                   @RequestParam("lname") String movieListName) throws UserNotFoundException {
        MovieListDTO insertedMovieList = movieListService.create(movieListName, userId);
        if (insertedMovieList == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(insertedMovieList, new HttpHeaders(), HttpStatus.CREATED);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteList(@RequestParam("userId") Long userId,
                                           @RequestParam("lname") String movieListName) throws UserNotFoundException {
        MovieListDTO deletedMovieList = movieListService.findListOfUser(userId, movieListName);
        if (deletedMovieList == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } else {
            movieListService.delete(deletedMovieList);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<MovieListDTO>> getUserLists(@RequestParam("userId") Long userId) {

        List<MovieListDTO> userLists = movieListService.findUserLists(userId);
        if (userLists == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(userLists);
    }

    @GetMapping("/list")
    public ResponseEntity<MovieListDTO> getMoviesFromList(@RequestParam("userId") Long userId,
                                                          @RequestParam("lname") String movieListName)
            throws UserNotFoundException {
        MovieListDTO movieListDTO = movieListService.findListOfUser(userId, movieListName);
        if (movieListDTO == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(movieListDTO);
    }

}

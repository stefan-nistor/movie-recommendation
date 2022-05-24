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
    public ResponseEntity<List<MovieListDTO>> getMovieLists()  {
        return ResponseEntity.ok().body(movieListService.findAll());
    }

    @GetMapping("/names")
    public ResponseEntity<MovieListDTO> getMovieListByName(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(movieListService.findByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<MovieListDTO> addMovieToList(@RequestParam("lname") String movieListName,
                                                       @RequestParam("mname") String movieName) {
        return ResponseEntity.ok().body(
                movieListService.addMovieToList(movieService.findByName(movieName,
                                Pageable.unpaged()),
                        movieListService.findByName(movieListName)));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<MovieListDTO> removeMovieFromList(@RequestParam("lname") String movieListName,
                                                            @RequestParam("mname") String movieName) {
        return ResponseEntity.ok().body(
                movieListService.removeMovieToList(movieService.findByName(movieName,
                                Pageable.unpaged()),
                        movieListService.findByName(movieListName)));
    }

    @PostMapping
    public ResponseEntity<?> createList(@RequestParam("lname") String movieListName,
                                        @RequestParam("user_id") Long userID) throws UserNotFoundException {
        MovieListDTO insertedMovieList = movieListService.create(movieListName,userID);
        if (insertedMovieList == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(insertedMovieList, new HttpHeaders(), HttpStatus.CREATED);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteList(@RequestParam("lname") String movieListName) {
        MovieListDTO deletedMovieList = movieListService.findByName(movieListName);
        if (deletedMovieList == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } else {
            movieListService.delete(deletedMovieList);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<MovieListDTO>> getUserLists(@RequestParam("user_id") Long userId){

        List<MovieListDTO> userLists=movieListService.findUserLists(userId);
        if(userLists == null){
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(userLists);
    }

    @GetMapping("/list")
    public ResponseEntity<MovieListDTO> getMoviesFromList(@RequestParam("user_id") Long userId,
                                                          @RequestParam("lname") String movieListName)
            throws UserNotFoundException {
        MovieListDTO movieListDTO=movieListService.findListOfUser(userId,movieListName);
        if(movieListDTO==null){
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(movieListDTO);
    }

}

package ro.info.uaic.movierecommendation.controllers.movielist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.lists.MovieListDTO;
import ro.info.uaic.movierecommendation.services.lists.MovieListService;
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

    @GetMapping("/searchN")
    public ResponseEntity<MovieListDTO> getMovieListByName(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(movieListService.findByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<MovieListDTO> addMovieToList(@RequestParam("MovieListName") String movieListName, @RequestParam("MovieName") String movieName) {
        return ResponseEntity.ok().body(movieListService.addMovieToList(movieService.findByName(movieName, Pageable.unpaged()).get(0), movieListService.findByName(movieListName)));
    }

    @PostMapping("/remove")
    public ResponseEntity<MovieListDTO> removeMovieFromList(@RequestParam("MovieListName") String movieListName, @RequestParam("MovieName") String movieName) {
        return ResponseEntity.ok().body(movieListService.removeMovieToList(movieService.findByName(movieName, Pageable.unpaged()).get(0), movieListService.findByName(movieListName)));
    }

}

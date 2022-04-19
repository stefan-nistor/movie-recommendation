package ro.info.uaic.movierecommendation.controllers.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.List;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovieList() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("searchN")
    public ResponseEntity<MovieDto> getMovieByName(@RequestParam("name") String name) {
        MovieDto foundMovie = service.findByName(name);
        return ResponseEntity.ok().body(foundMovie);
    }

    @GetMapping("searchT")
    public ResponseEntity<MovieDto> getMovieByType(@RequestParam("type") MovieType type) {
        MovieDto foundMovie = service.findByType(type);
        return ResponseEntity.ok().body(foundMovie);
    }

    @GetMapping("searchA")
    public ResponseEntity<MovieDto> getMovieByActor(@RequestParam("actor") String name) {
        MovieDto foundMovie = service.findByActor(name);
        return ResponseEntity.ok().body(foundMovie);
    }

}

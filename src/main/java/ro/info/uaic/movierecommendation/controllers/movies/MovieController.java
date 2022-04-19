package ro.info.uaic.movierecommendation.controllers.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovieList() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/searchN")
    public ResponseEntity<List<MovieDto>> getMovieByName(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(service.findByName(name));
    }

    @GetMapping("/searchT")
    public ResponseEntity<List<MovieDto>> getMovieByType(@RequestParam("type") MovieType type) {

        return ResponseEntity.ok().body(service.findByType(type));
    }

    @GetMapping("/searchA")
    public ResponseEntity<List<MovieDto>> getMovieByActor(@RequestParam("actor") String name) {
        return ResponseEntity.ok().body(service.findByActor(name));
    }


    @GetMapping("/filter")
    public ResponseEntity<List<MovieDto>> getThings(@RequestParam Map<String, String> requestParams) {

        String movieName = requestParams.get("movie");
        String typeMovie = requestParams.get("type");
        String actorMovie = requestParams.get("actor");

        return null;
    }

}

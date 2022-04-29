package ro.info.uaic.movierecommendation.controllers.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Type;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.List;
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
    public ResponseEntity<List<MovieDto>> getMovieByName(@RequestParam("name") String name) throws MovieNotFoundException {

        return new ResponseEntity(service.findByName(name), HttpStatus.OK);

    }

    @GetMapping("/searchT")
    public ResponseEntity<List<MovieDto>> getMovieByType(@RequestParam("type") List<Type> valuesType) throws MovieNotFoundException {

        return new ResponseEntity(service.findByType(valuesType), HttpStatus.OK);

    }

    @GetMapping("/searchA")
    public ResponseEntity<List<MovieDto>> getMovieByActor(@RequestParam("actor") List<String> valuesName) throws MovieNotFoundException {

        return new ResponseEntity(service.findByActor(valuesName), HttpStatus.OK);

    }


    @GetMapping("/filter")
    public ResponseEntity<List<MovieDto>> getMoviesByFilter(@RequestParam Map<String, String> requestParams) {

        String movieName = requestParams.get("movie");
        String typeMovie = requestParams.get("type");
        String actorMovie = requestParams.get("actor");

        return null;
    }

}

package ro.info.uaic.movierecommendation.controllers.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.Type;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovieList(@RequestParam Optional<Integer> page,
                                                       @RequestParam Optional<Integer> size, @RequestParam Optional<String> sortBy) throws MovieNotFoundException {

        return ResponseEntity.ok().body(service.findAll(PageRequest.of(
                page.orElse(0),
                size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id"))));
    }

    @GetMapping("/searchN")
    public ResponseEntity<List<MovieDto>> getMovieByName(@RequestParam("name") String name, @RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<Integer> size, @RequestParam Optional<String> sortBy) throws MovieNotFoundException {

        return new ResponseEntity(service.findByName(name, PageRequest.of(
                page.orElse(0),
                size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @GetMapping("/searchT")
    public ResponseEntity<List<MovieDto>> getMovieByType(@RequestParam("type") List<Type> valuesType, @RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<Integer> size, @RequestParam Optional<String> sortBy) throws MovieNotFoundException {

        return new ResponseEntity(service.findByType(valuesType, PageRequest.of(
                page.orElse(0),
                size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @GetMapping("/searchA")
    public ResponseEntity<List<MovieDto>> getMovieByActor(@RequestParam("actor") List<String> valuesName, @RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<Integer> size, @RequestParam Optional<String> sortBy) throws MovieNotFoundException {

        return new ResponseEntity(service.findByActor(valuesName, PageRequest.of(
                page.orElse(0),
                size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @PostMapping(value="/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMovie(@RequestBody MovieDto newMovie) {

        MovieDto insertedMovie=service.create(newMovie);
        if(insertedMovie == null){
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(insertedMovie, new HttpHeaders(), HttpStatus.CREATED);
        }
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable Long postId) {
        Optional<Movie> movieOptional = service.getMovieById(postId);

        if (movieOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!service.delete(movieOptional.get())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}

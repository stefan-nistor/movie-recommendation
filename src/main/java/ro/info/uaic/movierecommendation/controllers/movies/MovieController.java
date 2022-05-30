package ro.info.uaic.movierecommendation.controllers.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieRatingDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Type;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMovieList(@RequestParam Optional<Integer> page,
                                                            @RequestParam Optional<Integer> size,
                                                            @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return ResponseEntity.ok().body(service.findAll(PageRequest.of(page.orElse(0), size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id"))));
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Long id) throws MovieNotFoundException {
        return service.getMovieById(id);
    }

    @GetMapping("/top/{sizeTop}")
    public ResponseEntity<List<MovieDto>> getMovieTopList(@PathVariable int sizeTop,
                                                          @RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<Integer> size,
                                                          @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return ResponseEntity.ok().body(service.findMostVoted(PageRequest.of(
                page.orElse(0),
                size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id")), sizeTop));
    }

    public ResponseEntity<MovieDto> getMovieByName(@RequestParam("name") String name)
            throws MovieNotFoundException {

        return new ResponseEntity(service.findByName(name), HttpStatus.OK);
    }

    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getMovieByType(@RequestParam("type") List<Type> valuesType,
                                                              @RequestParam Optional<Integer> page,
                                                              @RequestParam Optional<Integer> size,
                                                              @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return new ResponseEntity<>(service.findByType(valuesType, PageRequest.of(page.orElse(0),
                size.orElse(5), Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @GetMapping("/actors")
    public ResponseEntity<List<MovieDto>> getMovieByActor(@RequestParam("actor") List<String> valuesName,
                                                          @RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<Integer> size,
                                                          @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return new ResponseEntity<>(service.findByActor(valuesName, PageRequest.of(page.orElse(0),
                size.orElse(5), Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> postMovie(@RequestBody MovieDto newMovie) {

        MovieDto insertedMovie = service.createMovie(newMovie);
        if (insertedMovie == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(insertedMovie, new HttpHeaders(), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long movieId) {
        if (!service.deleteMovie(movieId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{movieId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long movieId,
                                                @RequestBody MovieDto updatedMovie) throws MovieNotFoundException {
        return new ResponseEntity<>(service.updateMovie(movieId, updatedMovie),
                new HttpHeaders(), HttpStatus.RESET_CONTENT);
    }

    @PostMapping(value = "/singlePrediction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getSinglePrediction(@RequestBody UserMovieRatingDto userMovieRatingDto) {
        Boolean predictedLevel = service.getSinglePrediction(userMovieRatingDto);

        return new ResponseEntity<>(predictedLevel, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "/predictions/{noOfPredictions}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getPredictions(@PathVariable Integer noOfPredictions,
                                                              @RequestBody List<UserMovieRatingDto> userMovieRatingDtoList) {
        Map<String, Object> movieList = service.getPredictions(noOfPredictions, userMovieRatingDtoList);

        return new ResponseEntity<>(movieList, new HttpHeaders(), HttpStatus.OK);
    }
}

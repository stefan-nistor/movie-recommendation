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
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieLabelDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.Type;
import ro.info.uaic.movierecommendation.services.movies.MovieService;
import ro.info.uaic.movierecommendation.entites.UserMovieLabel;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovieList(@RequestParam Optional<Integer> page,
                                                       @RequestParam Optional<Integer> size,
                                                       @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {


        return ResponseEntity.ok().body(service.findAll(PageRequest.of(page.orElse(0), size.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id"))));
    }

    @GetMapping("{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) throws MovieNotFoundException {

        Optional<Movie> movieOptional = service.getMovieById(id);
        MovieDto foundMovie = mapper.map(movieOptional.get(), MovieDto.class);
        return ResponseEntity.ok().body(foundMovie);
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


    @GetMapping("/names")
    public ResponseEntity<MovieDto> getMovieByName(@RequestParam("name") String name,
                                                         @RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<Integer> size,
                                                         @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return new ResponseEntity(service.findByName(name, PageRequest.of(page.orElse(0),
                size.orElse(5), Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @GetMapping("/types")
    public ResponseEntity<List<MovieDto>> getMovieByType(@RequestParam("type") List<Type> valuesType,
                                                         @RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<Integer> size,
                                                         @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return new ResponseEntity(service.findByType(valuesType, PageRequest.of(page.orElse(0),
                size.orElse(5), Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @GetMapping("/actors")
    public ResponseEntity<List<MovieDto>> getMovieByActor(@RequestParam("actor") List<String> valuesName,
                                                          @RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<Integer> size,
                                                          @RequestParam Optional<String> sortBy)
            throws MovieNotFoundException {

        return new ResponseEntity(service.findByActor(valuesName, PageRequest.of(page.orElse(0),
                size.orElse(5), Sort.Direction.ASC, sortBy.orElse("id"))), HttpStatus.OK);

    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMovie(@RequestBody MovieDto newMovie) {

        MovieDto insertedMovie = service.createMovie(newMovie);
        if (insertedMovie == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(insertedMovie, new HttpHeaders(), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteById(@PathVariable Long movieId) {
        Optional<Movie> movieOptional = service.getMovieById(movieId);

        if (movieOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!service.deleteMovie(movieOptional.get())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{movieId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long movieId,
                                                @RequestBody MovieDto updatedMovie)
            throws MovieNotFoundException {
        Optional<Movie> movieOptional = service.getMovieById(movieId);

        if (movieOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Movie presentMovie = mapper.map(updatedMovie, Movie.class);
        presentMovie.setId(movieOptional.get().getId());
        updatedMovie = service.updateMovie(presentMovie);

        if (updatedMovie == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(updatedMovie, new HttpHeaders(), HttpStatus.RESET_CONTENT);
    }

    @PostMapping(value = "/singlePrediction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getSinglePrediction(@RequestBody UserMovieLabelDto userMovieLabelDto) {
        Boolean predictedLevel = service.getSinglePrediction(userMovieLabelDto);

        return new ResponseEntity<>(predictedLevel, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "/predictions/{noOfPredictions}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDto>> getPredictions(@PathVariable Integer noOfPredictions,
                                                         @RequestBody List<UserMovieLabelDto> userMovieLabelDtoList) {
        List<MovieDto> movieList = service.getPredictions(noOfPredictions, userMovieLabelDtoList);

        return new ResponseEntity<>(movieList, new HttpHeaders(), HttpStatus.OK);
    }

}

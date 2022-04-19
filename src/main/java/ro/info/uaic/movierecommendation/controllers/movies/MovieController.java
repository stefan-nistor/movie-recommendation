package ro.info.uaic.movierecommendation.controllers.movies;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.services.movies.MovieService;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovieList() {

        return ResponseEntity.ok().body(service.getMovies().stream().map(post -> modelMapper.map(post, MovieDto.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("searchN")
    public ResponseEntity<MovieDto> getMovieByName(@RequestParam("name") String name) {
        Optional<Movie> foundMovie = service.findByName(name);
        MovieDto movieResponse = modelMapper.map(foundMovie, MovieDto.class);

        return ResponseEntity.ok().body(movieResponse);
    }

    @GetMapping("searchT")
    public ResponseEntity<MovieDto> getMovieByType(@RequestParam("type") MovieType type) {
        Optional<Movie> foundMovie = service.findByType(type);
        MovieDto movieResponse = modelMapper.map(foundMovie, MovieDto.class);
        return ResponseEntity.ok().body(movieResponse);
    }

    @GetMapping("searchA")
    public ResponseEntity<MovieDto> getMovieByActor(@RequestParam("actor") String name) {
        Optional<Movie> foundMovie = service.findByActor(name);
        MovieDto movieResponse = modelMapper.map(foundMovie, MovieDto.class);
        return ResponseEntity.ok().body(movieResponse);
    }

}

package ro.info.uaic.movierecommendation.controllers.movielist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.info.uaic.movierecommendation.dtoresponses.lists.MovieListDTO;
import ro.info.uaic.movierecommendation.services.lists.MovieListService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movielists")
public class MovieListController {

    @Autowired
    private MovieListService movieListService;

    @GetMapping
    public ResponseEntity<List<MovieListDTO>> getMovieLists() {
        return ResponseEntity.ok().body(movieListService.findAll());
    }

    @GetMapping
    public ResponseEntity<MovieListDTO> getMovieListByName(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(movieListService.findByName(name));
    }
}

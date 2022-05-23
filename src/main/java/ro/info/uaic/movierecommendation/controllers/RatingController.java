package ro.info.uaic.movierecommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieLabelDto;
import ro.info.uaic.movierecommendation.entites.UserMovieLabel;
import ro.info.uaic.movierecommendation.exceptions.RatingNotFoundException;
import ro.info.uaic.movierecommendation.services.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/all")
    public ResponseEntity<List<UserMovieLabelDto>> getAllRatings() {
        return ResponseEntity.ok().body(ratingService.getAllRatings());
    }

    @GetMapping
    public ResponseEntity<UserMovieLabel> getRating(@RequestBody UserMovieLabelDto userMovieLabelDto)
            throws RatingNotFoundException {
        return ResponseEntity.ok().body(ratingService.findByIds(userMovieLabelDto));
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody UserMovieLabelDto userMovieLabelDto) {
        ratingService.createRating(userMovieLabelDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> removeRating(@RequestBody UserMovieLabelDto userMovieLabelDto) {
        if (!ratingService.deleteRating(userMovieLabelDto)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> changeRating(@RequestBody UserMovieLabelDto userMovieLabelDto) {
        ratingService.updateRating(userMovieLabelDto);

        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
}

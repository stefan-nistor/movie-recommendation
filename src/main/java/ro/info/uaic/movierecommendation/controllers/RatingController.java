package ro.info.uaic.movierecommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieLabelDto;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieRatingDto;
import ro.info.uaic.movierecommendation.entites.UserMovieRating;
import ro.info.uaic.movierecommendation.exceptions.RatingNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.services.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/all")
    public ResponseEntity<List<UserMovieRatingDto>> getAllRatings() {
        return ResponseEntity.ok().body(ratingService.getAllRatings());
    }

    @GetMapping("/u{userId}")
    public ResponseEntity<List<UserMovieRatingDto>> getRatingsForUser(@PathVariable Long userId)
            throws UserNotFoundException {
        return ResponseEntity.ok().body(ratingService.getRatingsForUser(userId));
    }

    @GetMapping("/m{movieId}")
    public ResponseEntity<List<UserMovieRatingDto>> getRatingsForMovie(@PathVariable Long movieId)
            throws UserNotFoundException {
        return ResponseEntity.ok().body(ratingService.getRatingsForMovie(movieId));
    }


    @GetMapping
    public ResponseEntity<UserMovieRating> getRating(@RequestBody UserMovieRatingDto userMovieRatingDto)
            throws RatingNotFoundException {
        return ResponseEntity.ok().body(ratingService.findByIds(userMovieRatingDto));
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody UserMovieRatingDto userMovieRatingDto) {
        ratingService.createRating(userMovieRatingDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> removeRating(@RequestParam Long userId, @RequestParam Long movieId) {
        if (!ratingService.deleteRating(userId, movieId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> changeRating(@RequestBody UserMovieRatingDto userMovieRatingDto) {
        ratingService.updateRating(userMovieRatingDto);

        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
}

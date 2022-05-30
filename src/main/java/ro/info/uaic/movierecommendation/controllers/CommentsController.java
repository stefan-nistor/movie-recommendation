package ro.info.uaic.movierecommendation.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.CommentDTO;
import ro.info.uaic.movierecommendation.entites.Comment;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;
import ro.info.uaic.movierecommendation.services.CommentsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentsController {


    @Autowired
    private CommentsService commentsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> postComment(@RequestBody CommentDTO newComment) throws UserException {
        CommentDTO insertedComment = commentsService.saveNewComment(newComment);
        if (insertedComment == null) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(insertedComment, new HttpHeaders(), HttpStatus.CREATED);
        }

    }

    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> updateMovie(@PathVariable Long commentId, @RequestBody String newContent)
            throws MovieNotFoundException {

        CommentDTO updatedComment = commentsService.updateComment(commentId, newContent);

        if (updatedComment == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(updatedComment, new HttpHeaders(), HttpStatus.RESET_CONTENT);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long commentId) {
        Optional<Comment> commentOptional = commentsService.getCommentById(commentId);

        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        commentsService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByMovieId(@RequestParam("movieId") Long movieId) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        if (movieOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(commentsService.getCommentsByMovieId(movieId), new HttpHeaders(), HttpStatus.OK);
        }
    }

    @GetMapping("/ids")
    public ResponseEntity<List<CommentDTO>> getCommentsByMovieIdAndUserId(@RequestParam("movieId") Long movieId,
                                                                          @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(commentsService.getCommentsByMovieIdAndUserId(movieId, userId),
                new HttpHeaders(), HttpStatus.OK);

    }
}

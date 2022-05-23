package ro.info.uaic.movierecommendation.controllers.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.CommentDTO;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.models.movies.Comment;
import ro.info.uaic.movierecommendation.services.movies.CommentsService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentsController {


    @Autowired
    private CommentsService commentsService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping
    public ResponseEntity<?> postComment(@RequestBody CommentDTO newComment) throws UserException {
        CommentDTO insertedComment=commentsService.saveNewComment(newComment);
        if(insertedComment == null){
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(insertedComment, new HttpHeaders(), HttpStatus.CREATED);
        }

    }

    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> updateMovie(@PathVariable Long commentId, @RequestBody CommentDTO updatedComment) throws MovieNotFoundException {
        Optional<Comment> commentOptional = commentsService.getCommentById(commentId);

        if (commentOptional.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CommentDTO presentComment = mapper.map(updatedComment, CommentDTO.class);
        presentComment.setId(commentOptional.get().getId());
        updatedComment = commentsService.updateComment(presentComment);

        if (updatedComment == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(updatedComment, new HttpHeaders(), HttpStatus.RESET_CONTENT);
    }

}

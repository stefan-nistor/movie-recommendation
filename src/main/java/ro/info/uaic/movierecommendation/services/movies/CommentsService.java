package ro.info.uaic.movierecommendation.services.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ro.info.uaic.movierecommendation.dtoresponses.movies.CommentDTO;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.models.movies.Comment;
import ro.info.uaic.movierecommendation.repositories.movies.CommentRepository;

import java.util.Optional;

public class CommentsService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper mapper;

    public CommentDTO saveNewComment(CommentDTO comment) throws UserException {
        commentRepository.save(mapper.map(comment, Comment.class));
        return comment;
    }

    public CommentDTO updateComment(CommentDTO comment) {
        var commentFromDb = commentRepository.findById(comment.getId());
        commentRepository.save(mapper.map(comment, Comment.class));
        return comment;
    }

    public Optional<Comment> getCommentById(long id){
        return commentRepository.findById(id);
    }
}

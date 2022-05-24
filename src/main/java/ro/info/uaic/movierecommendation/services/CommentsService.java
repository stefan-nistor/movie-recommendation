package ro.info.uaic.movierecommendation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.CommentDTO;
import ro.info.uaic.movierecommendation.entites.Comment;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.repos.CommentRepository;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MovieRepository movieRepository;

    public CommentDTO saveNewComment(CommentDTO comment) throws UserException {
        Comment comment1 = new Comment();

        if(!userRepo.existsById(comment.getId_user()))
            return null;

        if(!movieRepository.existsById(comment.getId_movie()))
            return null;
        comment1.setUser(userRepo.findById(comment.getId_user()).get());
        comment1.setMovie(movieRepository.findById(comment.getId_movie()).get());
        comment1.setContent(comment.getContent());
        commentRepository.save(comment1);
        return comment;
    }

    public CommentDTO updateComment(Long id, String content) {
        Optional<Comment> commentToUpdate = commentRepository.findById(id);

        if(commentToUpdate.isEmpty()){
            return null;
        }


        commentToUpdate.get().setContent(content);
        commentRepository.save(commentToUpdate.get());

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentToUpdate.get().getId());
        commentDTO.setId_user(commentToUpdate.get().getUser().getId());
        commentDTO.setId_movie(commentToUpdate.get().getMovie().getId());
        commentDTO.setContent(commentDTO.getContent());
        return commentDTO;
    }

    public Optional<Comment> getCommentById(Long id){
        return commentRepository.findById(id);
    }

    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByMovieId(Long movieId){
        Optional<Movie> movie = movieRepository.findById(movieId);

        if(movie.isEmpty())
            return null; //to add an exception

            return commentRepository.findByMovie(movie.get());
    }

    public List<Comment> getCommentsByMovieIdAndUserId(Long movieId, Long userId){
        Optional<Movie> movie = movieRepository.findById(movieId);
        Optional<UserEntity> user = userRepo.findById(userId);

        if(movie.isEmpty())
            return null; //to add an exception

        if(user.isEmpty())
            return null; //to add another exception

        return commentRepository.findByMovie(movie.get());
    }
}

package ro.info.uaic.movierecommendation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.CommentDTO;
import ro.info.uaic.movierecommendation.dtoresponses.UserObj;
import ro.info.uaic.movierecommendation.entites.Comment;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
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
        Optional<Movie> movie = movieRepository.findById(comment.getMovieId());
        Optional<UserEntity> user = userRepo.findById(comment.getUserId());

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for this id.");
        }

        Comment comment1 = new Comment();
        if (userRepo.findById(comment.getUserId()).isEmpty()) {
            throw new UserNotFoundException("User not found for this id.");
        }
        if (movieRepository.findById(comment.getMovieId()).isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "Movie not found for this id.");
        }

        comment1.setUser(userRepo.findById(comment.getUserId()).get());
        comment1.setMovie(movieRepository.findById(comment.getMovieId()).get());
        comment1.setContent(comment.getContent());
        comment1.setDate(comment.getDate());
        commentRepository.save(comment1);
        return comment;
    }

    public CommentDTO updateComment(Long id, String content) {
        Optional<Comment> commentToUpdate = commentRepository.findById(id);

        if (commentToUpdate.isEmpty()) {
            return null;
        }


        commentToUpdate.get().setContent(content);
        commentRepository.save(commentToUpdate.get());

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentToUpdate.get().getId());
        commentDTO.setUserId(commentToUpdate.get().getUser().getId());
        commentDTO.setMovieId(commentToUpdate.get().getMovie().getId());
        commentDTO.setUserObj(mapper.map(commentToUpdate.get().getUser(), UserObj.class));
        commentDTO.setContent(commentToUpdate.get().getContent());
        commentDTO.setDate(commentToUpdate.get().getDate());
        return commentDTO;
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<CommentDTO> getCommentsByMovieId(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }

        List<Comment> comments = commentRepository.findByMovie(movie.get());
        return comments.stream()
                .map(comment -> new CommentDTO(comment.getId(), comment.getMovie().getId(),
                        comment.getUser().getId(), mapper.map(comment.getUser(), UserObj.class),
                        comment.getContent(), comment.getDate())).toList();
    }

    public List<CommentDTO> getCommentsByMovieIdAndUserId(Long movieId, Long userId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        Optional<UserEntity> user = userRepo.findById(userId);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(Movie.class, "id");
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for this id.");
        }

        List<Comment> comments = commentRepository.findByUserAndMovie(user.get(), movie.get());
        return comments.stream()
                .map(comment -> new CommentDTO(comment.getId(), comment.getMovie().getId(),
                        comment.getUser().getId(), mapper.map(comment.getUser(), UserObj.class),
                        comment.getContent(), comment.getDate())).toList();
    }
}

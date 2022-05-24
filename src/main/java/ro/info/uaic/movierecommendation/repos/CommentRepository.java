package ro.info.uaic.movierecommendation.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.entites.Comment;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUser(UserEntity user);
    List<Comment> findByMovie(Movie movie);
    List<Comment> findByUserAndMovie(UserEntity user, Movie movie);
}

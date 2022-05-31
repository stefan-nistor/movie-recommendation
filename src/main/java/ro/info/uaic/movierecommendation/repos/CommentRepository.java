package ro.info.uaic.movierecommendation.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.entites.Comment;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserOrderByDateDesc(UserEntity user);
    List<Comment> findByMovieOrderByDateDesc(Movie movie);
    List<Comment> findByUserAndMovieOrderByDateDesc(UserEntity user, Movie movie);
}

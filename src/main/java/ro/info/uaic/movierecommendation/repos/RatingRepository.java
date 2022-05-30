package ro.info.uaic.movierecommendation.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.entites.UserMovieRating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<UserMovieRating, Long> {
    Optional<UserMovieRating> findByUserIdAndMovieId(String userId, String movieId);
    List<UserMovieRating> findByUserId(String userId);
    List<UserMovieRating> findByMovieId(String movieId);
}

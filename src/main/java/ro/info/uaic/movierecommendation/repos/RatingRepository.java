package ro.info.uaic.movierecommendation.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.entites.UserMovieLabel;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<UserMovieLabel, Long> {
    Optional<UserMovieLabel> findByUserIdAndMovieId(String userId, String movieId);
    List<UserMovieLabel> findByUserId(String userId);
}

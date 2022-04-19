package ro.info.uaic.movierecommendation.repositories.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
}

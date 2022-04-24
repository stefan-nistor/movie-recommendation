package ro.info.uaic.movierecommendation.repositories.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.dtoresponses.movies.ActorDto;
import ro.info.uaic.movierecommendation.models.movies.Actor;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.models.movies.Type;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    Optional<Movie> findByName(String name);
    Optional<Movie> findByTypeIn(List<MovieType> type);
    Optional<Movie> findByActorsIn(List<Actor> actor);
}

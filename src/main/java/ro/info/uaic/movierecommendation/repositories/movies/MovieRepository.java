package ro.info.uaic.movierecommendation.repositories.movies;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.movies.Actor;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByName(String name);
    Page<Movie> findByName(String name, Pageable paging);
    Page<Movie> findByTypeIn(List<MovieType> type, Pageable paging);
    Page<Movie> findByActorsIn(List<Actor> actor, Pageable paging);
    List<Movie> findByTypeIn(List<MovieType> types);
    Long countByIsDeleted(Boolean isDeleted);
}

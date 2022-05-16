package ro.info.uaic.movierecommendation.repositories.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.lists.MovieList;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {

    Optional<MovieList> findByName(String name);

    Optional<MovieList> findByid(Long idMovieList);


}

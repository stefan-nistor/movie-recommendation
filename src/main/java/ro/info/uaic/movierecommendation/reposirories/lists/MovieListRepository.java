package ro.info.uaic.movierecommendation.reposirories.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.lists.MovieList;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {

    Optional<MovieList> findByName(String name);

    Optional<MovieList> findByID(long idMovieList);

    Optional<MovieList> addMovieToList(Movie movie,long idMovieList);

    Optional<MovieList> removeMovieFromList(Movie movie, long idMovieList);

}

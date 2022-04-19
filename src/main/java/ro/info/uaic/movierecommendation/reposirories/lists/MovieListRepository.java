package ro.info.uaic.movierecommendation.reposirories.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.lists.MovieList;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, UUID> {

    Optional<MovieList> findByName(String name);

    Optional<Movie> addMovieToList(Movie movie, MovieList movieList);

    Optional<Movie> removeMovieFromList(Movie movie, MovieList movieList);

}

package ro.info.uaic.movierecommendation.repositories.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.models.movies.MovieList;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {
    Optional<MovieList> findByName(String name);
    Optional<List<MovieList>> findByUser(UserEntity user);
    Optional<MovieList> findByUserAndName(UserEntity user,String name);

}

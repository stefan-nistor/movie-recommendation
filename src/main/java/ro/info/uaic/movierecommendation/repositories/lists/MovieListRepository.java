package ro.info.uaic.movierecommendation.repositories.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.lists.MovieList;

import java.util.Optional;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {
    Optional<MovieList> findByName(String name);
}

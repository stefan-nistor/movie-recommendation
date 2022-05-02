package ro.info.uaic.movierecommendation.repositories.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.models.movies.Type;
import java.util.List;

public interface MovieTypeRepository extends JpaRepository<MovieType, Long> {

    List<MovieType> findByTypeIn(List<Type> type);
}

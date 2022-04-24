package ro.info.uaic.movierecommendation.repositories.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.models.movies.Type;

import java.util.List;
import java.util.UUID;

public interface MovieTypeRepository extends JpaRepository<MovieType, UUID> {

    List<MovieType> findByTypeIn(List<Type> type);
}

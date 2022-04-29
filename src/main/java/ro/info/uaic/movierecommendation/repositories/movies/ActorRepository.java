package ro.info.uaic.movierecommendation.repositories.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.info.uaic.movierecommendation.models.movies.Actor;

import java.util.List;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {

    List<Actor> findByNameIn(List<String> actorName);
}

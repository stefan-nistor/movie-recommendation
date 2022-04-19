package ro.info.uaic.movierecommendation.services.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.Actor;
import ro.info.uaic.movierecommendation.repositories.movies.ActorRepository;

import java.util.List;

@Service
public class ActorService {

    @Autowired
    private ActorRepository repositoryActor;

    public Actor findByName(String actorName) {

        return repositoryActor.findByName(actorName);
    }
}

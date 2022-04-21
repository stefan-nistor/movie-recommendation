package ro.info.uaic.movierecommendation.services.movies;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.movies.ActorDto;
import ro.info.uaic.movierecommendation.repositories.movies.ActorRepository;

@Service
public class ActorService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ActorRepository repositoryActor;

    public ActorDto findByName(String actorName) {

        return modelMapper.map(repositoryActor.findByName(actorName), ActorDto.class);
    }
}

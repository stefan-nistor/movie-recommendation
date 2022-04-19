package ro.info.uaic.movierecommendation.services.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dao.movies.MovieDao;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;

import java.util.*;

@Service
public class MovieService {

    MovieDao movieDAO;

    @Autowired
    public MovieService(@Qualifier("movieDao") MovieDao movieDao) {
        this.movieDAO = movieDao;
    }

    public List<Movie> getMovies() {
        return movieDAO.getMovies();
    }

    public Optional<Movie> findByName(String name) {

        return Optional.ofNullable(movieDAO.findByName(name));

    }

    public Optional<Movie> findByType(MovieType type) {
        return Optional.ofNullable(movieDAO.findByType(type));

    }

    public Optional<Movie> findByActor(String actorName) {
        return Optional.ofNullable(movieDAO.findByActor(actorName));
    }


}

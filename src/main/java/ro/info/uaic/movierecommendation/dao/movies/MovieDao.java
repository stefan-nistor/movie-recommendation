package ro.info.uaic.movierecommendation.dao.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.movies.Actor;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;

import java.util.List;

@Repository("movieDao")
public class MovieDao {
    @Autowired
    private MovieRepository repository;


    public List<Movie> getMovies() {
        List<Movie> movieList = repository.findAll();
        if (movieList.isEmpty()) {
            return null;
        }
        return movieList;
    }

    public Movie findByName(String name){

        List<Movie> movieList = repository.findAll();
        if (!movieList.isEmpty()) {
            for(Movie movie : movieList){
                if(movie.getName().equals(name)){
                    return movie;
                }
            }
        }
        return null;
    }

    public Movie findByType(MovieType type) {
        List<Movie> movieList = repository.findAll();
        if (!movieList.isEmpty()) {
            for(Movie movie : movieList){
                if(movie.getType().equals(type)){
                    return movie;
                }
            }
        }
        return null;
    }

    public Movie findByActor(String actorName) {
        List<Movie> movieList = repository.findAll();

        if (!movieList.isEmpty()) {
            for(Movie movie : movieList){

                for(Actor actor : movie.getActors()){
                    if(actor.getName().equals(actorName)){
                        return movie;
                    }
                }
            }
        }
        return null;
    }

}

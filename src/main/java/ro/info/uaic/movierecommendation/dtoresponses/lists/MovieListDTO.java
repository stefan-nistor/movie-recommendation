package ro.info.uaic.movierecommendation.dtoresponses.lists;

import lombok.Data;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.Date;
import java.util.List;

@Data
public class MovieListDTO {
    private String name;
    private List<Movie> movies;
}

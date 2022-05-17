package ro.info.uaic.movierecommendation.dtoresponses.lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieListDTO {
    private String name;
    private List<Movie> movies;
}

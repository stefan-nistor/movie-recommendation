package ro.info.uaic.movierecommendation.dtoresponses.movies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.info.uaic.movierecommendation.dtoresponses.UserObj;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.models.movies.Movie;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieListDTO {
    private String name;
    private List<Movie> movies;
    private UserObj user;
}

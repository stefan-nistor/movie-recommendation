package ro.info.uaic.movierecommendation.dtoresponses.movies;

import lombok.Data;
import ro.info.uaic.movierecommendation.models.movies.Actor;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.models.movies.Photo;

import java.util.Date;
import java.util.List;

@Data
public class MovieDto {
    private String name;
    private String description;
    private List<Actor> actors;
    private MovieType type;
    private String duration;
    private String director;
    private String writer;
    private boolean hasCaptions;
    private Date releaseDate;
    private List<Photo> photos;
}

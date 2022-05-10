package ro.info.uaic.movierecommendation.dtoresponses.movies;

import com.sun.istack.NotNull;
import lombok.Data;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.models.movies.Photo;

import java.util.Date;
import java.util.List;


@Data
public class MovieDto {

    @NotNull
    private String name;
    @NotNull
    private String description;
    private List<ActorDto> actors;
    @NotNull
    private List<MovieType> type;
    @NotNull
    private String duration;
    private String director;
    private String writer;
    private boolean hasCaptions;
    private Date releaseDate;
    @NotNull
    private List<Photo> photos;
}

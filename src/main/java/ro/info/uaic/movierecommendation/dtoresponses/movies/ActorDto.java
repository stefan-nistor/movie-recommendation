package ro.info.uaic.movierecommendation.dtoresponses.movies;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class ActorDto {

    @NotNull
    private String name;
    private int numberOfAwards;

}

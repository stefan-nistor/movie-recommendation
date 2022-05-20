package ro.info.uaic.movierecommendation.dtoresponses.movies;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActorDto {

    @NotNull
    private String name;
    private int numberOfAwards;

    public ActorDto(String name) {
        this.name = name;
    }
}

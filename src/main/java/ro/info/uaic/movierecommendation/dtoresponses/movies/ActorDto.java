package ro.info.uaic.movierecommendation.dtoresponses.movies;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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

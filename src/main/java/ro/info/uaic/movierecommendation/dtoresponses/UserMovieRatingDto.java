package ro.info.uaic.movierecommendation.dtoresponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMovieRatingDto {
    private String userId;
    private String movieId;
    private Boolean label;
    private Integer rating;
}

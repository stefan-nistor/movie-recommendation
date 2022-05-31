package ro.info.uaic.movierecommendation.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usermovieratings")
@Builder
public class UserMovieRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String userId;
    private String movieId;
    @NotNull
    private Boolean label;
    @NotNull
    @Min(0)
    @Max(10)
    private Integer rating;
}

package ro.info.uaic.movierecommendation.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_movie_rating")
@Builder
public class UserMovieLabel {
    @Id
    private Long id;
    private String userId;
    private String movieId;
    private Boolean label;
}

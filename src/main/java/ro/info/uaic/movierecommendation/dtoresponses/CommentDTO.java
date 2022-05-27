package ro.info.uaic.movierecommendation.dtoresponses;

import lombok.*;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long movieId;
    private Long userId;
    private String content;
}

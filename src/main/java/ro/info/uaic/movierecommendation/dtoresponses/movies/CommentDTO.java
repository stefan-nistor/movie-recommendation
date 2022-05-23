package ro.info.uaic.movierecommendation.dtoresponses.movies;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private long id;
    private long id_movie;
    private long id_user;
    private String content;
}

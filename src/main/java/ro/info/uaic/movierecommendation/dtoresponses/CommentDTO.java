package ro.info.uaic.movierecommendation.dtoresponses;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long id_movie;
    private Long id_user;
    private String content;
}

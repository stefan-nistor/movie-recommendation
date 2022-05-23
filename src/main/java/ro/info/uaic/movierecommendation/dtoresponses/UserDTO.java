package ro.info.uaic.movierecommendation.dtoresponses;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String lastname;
    private String firstname;
    private String passwordToken;
}
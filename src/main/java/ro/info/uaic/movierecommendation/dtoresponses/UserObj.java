package ro.info.uaic.movierecommendation.dtoresponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserObj {
    private Long id;
    private String username;
    private String email;
    private String lastname;
    private String firstname;
}

package ro.info.uaic.movierecommendation.dtoresponses;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {
    private String resetToken;
    private String newPassword;
    private String confirmPassword;
}

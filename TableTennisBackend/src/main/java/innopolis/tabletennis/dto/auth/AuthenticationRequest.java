package innopolis.tabletennis.dto.auth;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Username or login must be specified and not blank")
    private String username;
    @NotBlank(message = "Password must be specified and not blank")
    private String password;
}

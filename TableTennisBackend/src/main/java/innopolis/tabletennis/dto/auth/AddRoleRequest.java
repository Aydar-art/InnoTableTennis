package innopolis.tabletennis.dto.auth;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleRequest {
    @NotBlank(message = "Username or login must be specified and not blank")
    private String username;
    @NotBlank(message = "Role must be specified and not blank")
    private String role;
}

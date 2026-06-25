package innopolis.tabletennis.dto.auth;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Username or login must be specified and not blank")
    private String username;
    @NotBlank(message = "Old password must be specified and not blank")
    private String oldPassword;
    @NotBlank(message = "New password must be specified and not blank")
    private String newPassword;
}

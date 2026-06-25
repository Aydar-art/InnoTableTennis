package innopolis.tabletennis.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderDto {
    @Null(message = "You can't specify the id of a leader")
    private Integer id;
    private String name;

    @NotBlank(message = "Telegram alias is required")
    @Size(max = 50, message = "Telegram alias must be less than or equal to 50 characters")
    private String telegramAlias;

}

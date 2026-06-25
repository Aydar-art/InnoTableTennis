package innopolis.tabletennis.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TelegramUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
}


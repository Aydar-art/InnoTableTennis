package innopolis.tabletennis.telegram;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramAuthResponse {
    private final String password;
}

package innopolis.tabletennis.kafka;

import innopolis.tabletennis.telegram.TelegramUser;
import lombok.Data;

@Data
public class KafkaTelegramUserUpdate {
    private TelegramUser oldUser;
    private TelegramUser updatedUser;
}

package innopolis.tabletennis.dto;

import lombok.*;

@AllArgsConstructor
@Getter
public class PlayerGraphPoint {
    private String date;
    private Float rating;
    private String tournamentTitle;
    private Float delta;
}

package innopolis.tabletennis.dto;

import lombok.*;

@AllArgsConstructor
@Getter
public class MatchWithOpponent {
    private final String opponentName;
    private final Float opponentRatingBefore;
    private final Float delta;
    private final String score;
    private final String date;
    private final String tournamentTitle;
}

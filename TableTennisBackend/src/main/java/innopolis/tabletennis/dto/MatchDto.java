package innopolis.tabletennis.dto;

import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.util.*;
import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {

    @NotNull(message = "First player score must be specified")
    @Min(value = 0, message = "Player score must be greater than or equal to 0")
    @Max(value = 10, message = "Player score must be less than 11")
    public Integer firstPlayerScore;
    @NotNull(message = "Second player score must be specified")
    @Min(value = 0, message = "Player score must be greater than or equal to 0")
    @Max(value = 10, message = "Player score must be less than 11")
    public Integer secondPlayerScore;
    @NotBlank(message = "First player name must be specified")
    private String firstPlayerName;
    @NotBlank(message = "Second player name must be specified")
    private String secondPlayerName;
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$", message = "Date must be in format dd.MM.yyyy")
    public String localDateString;

    @Null(message = "You are not allowed to send id of the match")
    public Integer id;
    @Null(message = "You are not allowed to send delta of rating")
    private Float firstPlayerRatingDelta;
    @Null(message = "You are not allowed to send delta of rating")
    private Float secondPlayerRatingDelta;
    @Null(message = "You are not allowed to send rating of a player before the match")
    private Float firstPlayerRatingBefore;
    @Null(message = "You are not allowed to send rating of a player before the match")
    private Float secondPlayerRatingBefore;

    @NotBlank(message = "Tournament title must be specified (not blank)")
    private String tournamentTitle;


    // to MatchDto from Match
    public static MatchDto from(Match match) {
        return MatchDto.builder()
                .firstPlayerScore(match.getFirstPlayerScore())
                .secondPlayerScore(match.getSecondPlayerScore())
                .firstPlayerName(match.getFirstPlayer().getName())
                .secondPlayerName(match.getSecondPlayer().getName())
                .localDateString(Util.getStringFromLocalDate(match.getDate()))
                .id(match.getId())
                .firstPlayerRatingDelta(match.getFirstPlayerRatingDelta())
                .secondPlayerRatingDelta(match.getSecondPlayerRatingDelta())
                .firstPlayerRatingBefore(match.getFirstPlayerRatingBefore())
                .secondPlayerRatingBefore(match.getSecondPlayerRatingBefore())
                .tournamentTitle(match.getTournament().getTitle())
                .build();
    }
}

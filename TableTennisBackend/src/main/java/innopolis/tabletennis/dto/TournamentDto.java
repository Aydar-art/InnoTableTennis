package innopolis.tabletennis.dto;

import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.util.*;
import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDto {
    private Integer id;
    private String title;
    //    @JsonIgnore
//    private List<MatchDto> matches;
    private String startDateString;
    private String endDateString;
    @Null(message = "You are not allowed to send tournament coefficient")
    private Float coefficient;
    @Null(message = "You are not allowed to send tournament finished flag")
    private Boolean finished;
    @Null(message = "You are not allowed to send number of players")
    private Integer numberOfPlayers;

    public static TournamentDto from(Tournament tournament) {
        return TournamentDto.builder()
                .id(tournament.getId())
                .title(tournament.getTitle())
//                .matches(tournament.getMatches().stream().map(MatchDto::from).collect(Collectors.toList()))
                .startDateString(Util.getStringFromLocalDate(tournament.getStartDate()))
                .endDateString(Util.getStringFromLocalDate(tournament.getEndDate()))
                .coefficient(tournament.getCoefficient())
                .finished(tournament.isFinished())
                .numberOfPlayers(tournament.getNumberOfPlayers())
                .build();
    }
}

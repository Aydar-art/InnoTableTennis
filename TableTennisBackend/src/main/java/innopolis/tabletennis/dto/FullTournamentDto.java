package innopolis.tabletennis.dto;


import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.util.*;
import lombok.*;

import java.util.*;
import java.util.stream.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullTournamentDto {
    private Integer id;
    private String title;
    private List<MatchDto> matches;
    private String startDateString;
    private String endDateString;
    private Float coefficient;
    private Boolean finished;
    private Integer numberOfPlayers;
    private String state; // Json representation of tables, list of players etc.

    public static FullTournamentDto from(Tournament tournament) {
        return FullTournamentDto.builder()
                .id(tournament.getId())
                .title(tournament.getTitle())
                .matches(tournament.getMatches().stream().map(MatchDto::from).collect(Collectors.toList()))
                .startDateString(Util.getStringFromLocalDate(tournament.getStartDate()))
                .endDateString(Util.getStringFromLocalDate(tournament.getEndDate()))
                .coefficient(tournament.getCoefficient())
                .finished(tournament.isFinished())
                .numberOfPlayers(tournament.getNumberOfPlayers())
                .state(tournament.getState())
                .build();
    }
}

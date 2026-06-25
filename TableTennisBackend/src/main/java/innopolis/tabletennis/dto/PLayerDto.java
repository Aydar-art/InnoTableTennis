package innopolis.tabletennis.dto;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public class PLayerDto {
    private String name;
    private String telegramAlias;
    private PlayerStatistics statistics;
    private List<MatchWithOpponent> matches;
    private List<PlayerGraphPoint> graph;
}

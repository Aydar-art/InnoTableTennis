package innopolis.tabletennis.service;

import innopolis.tabletennis.dto.*;
import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.exception.*;
import innopolis.tabletennis.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class PlayerStatisticsService {
    private final PlayerRepository playerRepository;

    public PlayerStatistics getStatsByPlayerId(int id) {
        Player player = this.playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Player with id %s was not found", id)));

        int ranking = (int) playerRepository.findAll().stream() // players that have greater rating
                .filter(p -> p.getRating().compareTo(player.getRating()) > 0).count() + 1;

        List<Match> matches = player.getMatches();

        // Sort by date
        matches.sort((m1, m2) -> m1.getDate().isBefore(m2.getDate()) ? -1 :
                m1.getDate().equals(m2.getDate()) && (m1.getId() < m2.getId()) ? -1 : 1);

        List<Match> wonMatches = matches.stream().filter(m -> m.getWinner().equals(player)).collect(Collectors.toList());
        List<Match> lostMatches = matches.stream().filter(m -> m.getLoser().equals(player)).collect(Collectors.toList());

        int matchesWon = wonMatches.size();
        int matchesLost = lostMatches.size();
        int pointsWon = matches.stream().mapToInt(m -> m.getPoints(player)).sum();
        int totalPoints = matches.stream().mapToInt(Match::getTotalPoints).sum();

        int winningStreak = 0;
        int maxWinningStreak = 0;
        for (Match m : matches) {
            if (m.getWinner().equals(player))
                winningStreak += 1;
            else winningStreak = 0;
            maxWinningStreak = Math.max(maxWinningStreak, winningStreak);
        }

        int losingStreak = 0;
        int maxLosingStreak = 0;
        for (Match m : matches) {
            if (!m.getWinner().equals(player))
                losingStreak += 1;
            else losingStreak = 0;
            maxLosingStreak = Math.max(maxLosingStreak, losingStreak);
        }

        Map<Tournament, List<Match>> tournaments = new HashMap<>();

        for (Match m : matches) {
            if (!tournaments.containsKey(m.getTournament()))
                tournaments.put(m.getTournament(), new LinkedList<>());
            tournaments.get(m.getTournament()).add(m);
        }

        List<Double> tournamentDeltas = tournaments.entrySet().stream()
                .map(e -> e.getValue().stream()
                        .mapToDouble(m -> m.getDelta(player)).sum()
                ).collect(Collectors.toList());

        float maxTournamentDelta = (float) tournamentDeltas.stream().mapToDouble(e -> e).max().orElseGet(() -> 0f);
        float minTournamentDelta = (float) tournamentDeltas.stream().mapToDouble(e -> e).min().orElseGet(() -> 0f);
        float avgTournamentDelta = (float) tournamentDeltas.stream().mapToDouble(e -> e).sum() / tournamentDeltas.size();


        return PlayerStatistics.builder()
                .matchesPlayed(matches.size())
                .matchesWon(matchesWon)
                .matchesLost(matchesLost)
                .winPercentage(matchesWon * 1f / (matchesLost + matchesWon))
                .pointsWon(pointsWon)
                .pointsLost(totalPoints - pointsWon)
                .averageMatchDuration((float) totalPoints / matches.size())
                .averageMatchPoints((float) pointsWon / matches.size())
                .winningStreak(maxWinningStreak)
                .losingStreak(maxLosingStreak)
                .highestMatchDelta(matches.stream().map(m -> m.getDelta(player)).max(Float::compareTo).orElseGet(() -> 0f))
                .lowestMatchDelta(matches.stream().map(m -> m.getDelta(player)).min(Float::compareTo).orElseGet(() -> 0f))
                .averageMatchDelta(matches.stream().map(m -> m.getDelta(player)).reduce(Float::sum).orElseGet(() -> 0f) / matches.size())
                .tournamentsParticipated(tournaments.size())
                .highestTournamentDelta(maxTournamentDelta)
                .lowestTournamentDelta(minTournamentDelta)
                .averageTournamentDelta(avgTournamentDelta)
                .rating(player.getRating())
                .ranking(ranking)
                .build();
    }
}

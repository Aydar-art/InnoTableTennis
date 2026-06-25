package innopolis.tabletennis.dto;

import lombok.*;

@Builder
@Getter
public class PlayerStatistics {
    // The total number of matches a player played
    private final int matchesPlayed;
    // The total number of tournaments a player has participated in
    private final int tournamentsParticipated;
    // The number of matches a player has won.
    private final int matchesWon;
    //Matches Lost: The number of matches a player has lost.
    private final int matchesLost;
    // The percentage of matches won out of the total matches played.
    private final float winPercentage;
    // The total number of points a player has won across all matches.
    private final int pointsWon;
    // The total number of points a player has lost across all matches.
    private final int pointsLost;
    // The average number of points in match with a player
    private final float averageMatchDuration;
    // The average number of points scored by a player in each match
    private final float averageMatchPoints;
    // The longest consecutive winning streak of a player.
    private final int winningStreak;
    // The longest consecutive losing streak of a player.
    private final int losingStreak;

    // An overall performance based on match results.
    private final float highestMatchDelta;
    private final float lowestMatchDelta;
    private final float averageMatchDelta;

    // An overall performance based on tournament results.
    private final float highestTournamentDelta;
    private final float lowestTournamentDelta;
    private final float averageTournamentDelta;

    // The current ranking position of a player in the system.
    private final int ranking;
    // Rating in the ranking system
    private final float rating;
}

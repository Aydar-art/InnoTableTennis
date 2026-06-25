package innopolis.tabletennis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import innopolis.tabletennis.dto.MatchDto;
import innopolis.tabletennis.util.Util;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tennis_match")
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "first_player_id", nullable = false)
    private Player firstPlayer;
    @ManyToOne
    @JoinColumn(name = "second_player_id", nullable = false)
    private Player secondPlayer;

    @Column(name = "score_first_player", nullable = false)
    private Integer firstPlayerScore; // score of first and second separately
    @Column(name = "score_second_player", nullable = false)
    private Integer secondPlayerScore;
    @Column(name = "first_player_rating_delta", precision = 2, nullable = false)
    private Float firstPlayerRatingDelta;
    @Column(name = "second_player_rating_delta", precision = 2, nullable = false)
    private Float secondPlayerRatingDelta;

    @Column(name = "first_player_rating_before", precision = 2, nullable = false)
    private Float firstPlayerRatingBefore;
    @Column(name = "second_player_rating_before", precision = 2, nullable = false)
    private Float secondPlayerRatingBefore;

    @ManyToOne
    @JoinColumn(name = "tournament", nullable = false)
    private Tournament tournament;

    public static Match from(MatchDto matchDto) {
        Match match = new Match();

        // Date
        LocalDate date;
        if (matchDto.localDateString == null)
            date = LocalDate.now();
        else {
            date = Util.getLocalDateFromString(matchDto.getLocalDateString());
        }

        match.setDate(date);
        match.setFirstPlayerScore(matchDto.firstPlayerScore);
        match.setSecondPlayerScore(matchDto.secondPlayerScore);
        match.setFirstPlayerScore(matchDto.firstPlayerScore);
        match.setSecondPlayerScore(matchDto.secondPlayerScore);

        return match;
    }

    public Match() {
    }

    @Override
    public String toString() {
        return "Match{" +
            "id=" + id +
            ", date=" + date +
            ", firstPlayer=" + firstPlayer +
            ", secondPlayer=" + secondPlayer +
            ", scoreOfFirstPlayer=" + firstPlayerScore +
            ", scoreOfSecondPlayer=" + secondPlayerScore +
            ", firstPlayerRatingDelta=" + firstPlayerRatingDelta +
            ", secondPlayerRatingDelta=" + secondPlayerRatingDelta +
            '}';
    }

    @JsonIgnore
    public Player getWinner() {
        if (firstPlayerScore.compareTo(secondPlayerScore) > 0)
            return firstPlayer;
        return secondPlayer;
    }

    @JsonIgnore
    public Player getLoser() {
        if (firstPlayerScore.compareTo(secondPlayerScore) < 0)
            return firstPlayer;
        return secondPlayer;
    }

    @JsonIgnore
    public int getPoints(Player player) {
        if (firstPlayer.equals(player))
            return firstPlayerScore;
        return secondPlayerScore;
    }

    @JsonIgnore
    public int getTotalPoints() {
        return firstPlayerScore + secondPlayerScore;
    }

    @JsonIgnore
    public float getDelta(Player player) {
        if (firstPlayer.equals(player)) {
            return firstPlayerRatingDelta;
        }
        return secondPlayerRatingDelta;
    }

    @JsonIgnore
    public int getWinnerScore() {
        if (firstPlayerScore.compareTo(secondPlayerScore) > 0)
            return firstPlayerScore;
        return secondPlayerScore;
    }

    @JsonIgnore
    public int getLoserScore() {
        if (firstPlayerScore.compareTo(secondPlayerScore) < 0)
            return firstPlayerScore;
        return secondPlayerScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        return id == match.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

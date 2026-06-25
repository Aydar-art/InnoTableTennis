package innopolis.tabletennis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "tennis_player")
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "player_name", nullable = false, unique = true)
    private String name;
    @Column(name = "telegram_alias", unique = true)
    private String telegramAlias;

    @Column(name = "number_of_wins", nullable = false)
    private Integer numberOfWins;
    @Column(name = "number_of_loses", nullable = false)
    private Integer numberOfLosses;
    @Column(name = "rating", precision = 2, nullable = false)
    private Float rating;
    @Transient
    private Float winRate;
    @Transient
    private Integer numberOfGames;

    @OneToMany(mappedBy = "firstPlayer")
    @JsonIgnore
    private List<Match> matchesAsFirstPlayer;
    @OneToMany(mappedBy = "secondPlayer")
    @JsonIgnore
    private List<Match> matchesAsSecondPlayer;

    @PostLoad
    public void init() {
        numberOfGames = numberOfLosses + numberOfWins;
        if (numberOfGames == 0)
            winRate = 0f;
        else
            winRate = (float) numberOfWins / numberOfGames;
    }

    public Player() {
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", telegramAlias='" + telegramAlias + '\'' +
            ", numberOfWins=" + numberOfWins +
            ", numberOfLosses=" + numberOfLosses +
            '}';
    }

    @JsonIgnore
    public List<Match> getMatches() {
        return Stream.concat(this.matchesAsFirstPlayer.stream(),
            this.matchesAsSecondPlayer.stream()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

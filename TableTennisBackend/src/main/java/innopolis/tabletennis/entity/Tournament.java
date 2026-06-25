package innopolis.tabletennis.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "tournament")
    List<Match> matches;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "coefficient")
    private Float coefficient;

    @Column(name = "finished")
    private boolean finished;

    @Column(name = "number_of_players", nullable = false, columnDefinition = "integer default 0")
    private Integer numberOfPlayers;

    @Column(name = "state", columnDefinition = "varchar")
    private String state; // json

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournament that = (Tournament) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getNumberOfPlayers() {
        if (numberOfPlayers != null) return numberOfPlayers;

        Set<Player> players = new HashSet<>();

        matches.forEach(m -> {
            players.add(m.getFirstPlayer());
            players.add(m.getSecondPlayer());
        });

        return numberOfPlayers = players.size();
    }
}

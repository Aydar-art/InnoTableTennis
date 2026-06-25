package innopolis.tabletennis.repository;

import innopolis.tabletennis.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}

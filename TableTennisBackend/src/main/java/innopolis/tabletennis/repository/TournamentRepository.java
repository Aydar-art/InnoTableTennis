package innopolis.tabletennis.repository;

import innopolis.tabletennis.entity.Tournament;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findByTitle(String title);

    List<Tournament> findAllBy(Sort sort);

    List<Tournament> findAllByStartDateGreaterThanEqual(LocalDate startDate, Sort sort);

}
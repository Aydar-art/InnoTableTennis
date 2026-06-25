package innopolis.tabletennis.repository;

import innopolis.tabletennis.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByName(String name);

    Player findByTelegramAlias(String alias);

    List<Player> findAllByOrderByRatingDescIdDesc();

    List<Player> findAllByOrderByNameDesc();

    List<Player> findAllByTelegramAliasIn(List<String> aliases);
}

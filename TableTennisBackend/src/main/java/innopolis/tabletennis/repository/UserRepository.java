package innopolis.tabletennis.repository;

import innopolis.tabletennis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByTelegramChatId(Long chatId);

    List<User> findAllByRolesName(String roleName);
}

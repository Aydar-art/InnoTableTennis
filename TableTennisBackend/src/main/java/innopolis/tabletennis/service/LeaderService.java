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
public class LeaderService {
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final AuthenticationService authService;

    public List<LeaderDto> findAllLeaders() {
        List<String> aliases = userRepository.findAllByRolesName("ROLE_LEADER")
                .stream().map(User::getUsername).collect(Collectors.toList());
        List<Player> players = playerRepository.findAllByTelegramAliasIn(aliases);

        return players.stream().map(p ->
                new LeaderDto(p.getId(), p.getName(), p.getTelegramAlias())
        ).collect(Collectors.toList());
    }

    public LeaderDto addLeader(LeaderDto leaderDto) {
        Player player = playerRepository.findByTelegramAlias(
                leaderDto.getTelegramAlias()
        );

        if (player == null)
            throw new NotFoundException("Player with alias " + leaderDto.getTelegramAlias() + " was not found");

        authService.addRoleToUser(player.getTelegramAlias(), "LEADER");

        return new LeaderDto(player.getId(), player.getTelegramAlias(), player.getName());
    }

    public boolean deleteLeader(Integer id) {
        return authService.deleteRole(
                "LEADER",
                playerRepository.findById(id)
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Plaer with id " + id + " is not found"
                                )
                        ).getTelegramAlias()
        );
    }
}

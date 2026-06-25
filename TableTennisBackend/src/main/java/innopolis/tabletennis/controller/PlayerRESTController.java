package innopolis.tabletennis.controller;

import innopolis.tabletennis.dto.MatchWithOpponent;
import innopolis.tabletennis.dto.PLayerDto;
import innopolis.tabletennis.dto.PlayerGraphPoint;
import innopolis.tabletennis.dto.PlayerStatistics;
import innopolis.tabletennis.entity.Match;
import innopolis.tabletennis.entity.Player;
import innopolis.tabletennis.entity.Tournament;
import innopolis.tabletennis.exception.NotFoundException;
import innopolis.tabletennis.repository.PlayerRepository;
import innopolis.tabletennis.responses.ApiResponse;
import innopolis.tabletennis.service.AuthenticationService;
import innopolis.tabletennis.service.PlayerStatisticsService;
import innopolis.tabletennis.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Slf4j
public class PlayerRESTController {
    private final PlayerRepository playerRepository;
    private final AuthenticationService authService;
    private final PlayerStatisticsService statisticsService;

    @CrossOrigin
    @GetMapping
    public List<Player> getPlayers(@RequestParam(name = "page", required = false) Integer page,
                                   @RequestParam(name = "size", required = false) Integer size,
                                   @RequestParam(name = "sortBy", required = false, defaultValue = "rating") String sortBy,
                                   @RequestParam(name = "descending", required = false, defaultValue = "true") boolean descending,
                                   @RequestParam(name = "nameHas", required = false, defaultValue = "") String nameHas,
                                   @RequestParam(name = "aliasHas", required = false, defaultValue = "") String aliasHas,
                                   @RequestParam(name = "minRating", required = false, defaultValue = "0") float minRating,
                                   @RequestParam(name = "maxRating", required = false, defaultValue = "10000") float maxRating,
                                   HttpServletResponse response) {
        if (page != null) // need to prepare page
            page -= 1; // the page starts from 1 for client
        else page = 0; // first page by default is returned if not specified

        if (size == null) // size is not specified
            size = 10; // default size

        Pageable pageable = PageRequest.of(page, size);

        List<Player> content;
        if (sortBy.equals("name")) content = playerRepository.findAllByOrderByNameDesc();
        else content = playerRepository.findAllByOrderByRatingDescIdDesc();

        content = content.stream().filter(
            p -> p.getRating().compareTo(minRating) >= 0 &&
                p.getRating().compareTo(maxRating) <= 0 &&
                p.getName().toLowerCase().contains(nameHas.toLowerCase()) &&
                ((p.getTelegramAlias() == null &&
                    (aliasHas.equals("null") || aliasHas.length() == 0)
                ) || (p.getTelegramAlias() != null &&
                    p.getTelegramAlias().toLowerCase().contains(aliasHas.toLowerCase())
                )
                )
        ).collect(Collectors.toList());

        if (!descending) {
            Collections.reverse(content);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        Page<Player> playerPage = new PageImpl<>(content.subList(start, end), pageable, content.size());

        int totalPages = playerPage.getTotalPages();
        response.setHeader("X-Total-Pages", String.valueOf(totalPages));


        return playerPage.getContent();
    }

    @PostMapping
    public Player savePlayer(@RequestBody Player player) {
        if (player == null) throw new IllegalArgumentException("Player was not specified!");
        if (player.getName() == null) throw new IllegalArgumentException("Player name must be specified");
        if (player.getId() != null)
            throw new IllegalArgumentException("Can`t update player (with ID) via POST request. Use PUT instead");
        if (playerRepository.findByName(player.getName()) != null)
            throw new IllegalArgumentException("Player name must be unique");
        if (player.getTelegramAlias() != null && playerRepository.findByTelegramAlias(player.getTelegramAlias()) != null)
            throw new IllegalArgumentException("Player telegram alias must be unique");

        if (player.getRating() == null) player.setRating(100f);

        player.setNumberOfWins(0);
        player.setNumberOfLosses(0);

        if (player.getTelegramAlias() != null) player.setTelegramAlias(player.getTelegramAlias().replace("@", ""));

        return playerRepository.save(player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deletePlayer(@PathVariable("id") int id) {
        Optional<Player> playerCandidate = playerRepository.findById(id);
        if (playerCandidate.isPresent()) {
            Player player = playerCandidate.get();
            if (!player.getMatchesAsFirstPlayer().isEmpty() || !player.getMatchesAsSecondPlayer().isEmpty())
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            playerRepository.delete(player);
            authService.deleteUserByUsername(player.getTelegramAlias());
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<PlayerStatistics> getPlayerStatistics(@PathVariable("id") int id) {
        PlayerStatistics stats = this.statisticsService.getStatsByPlayerId(id);
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlayer(@PathVariable("id") Integer id, @RequestBody Player player) {
        log.info("Trying to update player with id {}", id);
        if (player == null) throw new IllegalArgumentException("Player was not specified!");
        if (player.getName() == null) throw new IllegalArgumentException("Player name must be specified");
        if (player.getId() != null)
            throw new IllegalArgumentException("Can't update player with Id in the body. Use path variable.");

        Player old = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player with id " + id + " was not found"));

        if (!old.getName().equals(player.getName()) && playerRepository.findByName(player.getName()) != null)
            throw new IllegalArgumentException("Player's new name must be unique");
        old.setName(player.getName());

        // Unregister player
        if (old.getTelegramAlias() != null && player.getTelegramAlias() == null) {
            authService.deleteUserByUsername(old.getTelegramAlias());
            old.setTelegramAlias(null);
        }

        // Change telegram alias (not null)
        if (old.getTelegramAlias() != null && player.getTelegramAlias() != null && !old.getTelegramAlias().equals(player.getTelegramAlias())) {
            if (playerRepository.findByTelegramAlias(player.getTelegramAlias()) != null)
                throw new IllegalArgumentException("Player's new alias must be unique");
            String newAlias = player.getTelegramAlias().replace("@", "").trim();
            authService.changeUsername(old.getTelegramAlias(), newAlias);
            old.setTelegramAlias(newAlias);
        }

        // Register
        if (old.getTelegramAlias() == null && player.getTelegramAlias() != null) {
            old.setTelegramAlias(player.getTelegramAlias());
            playerRepository.save(old);
            authService.registerByUsername(player.getTelegramAlias());
        }

        // update rating
        if (player.getRating() != null) old.setRating(player.getRating());

        playerRepository.save(old);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idOrAlias}")
    public ResponseEntity<ApiResponse<PLayerDto>> getPlayerById(@PathVariable("idOrAlias") String idOrAlias) {
        Integer id;
        try {
            id = Integer.parseInt(idOrAlias);
        } catch (NumberFormatException e) {
            Player temp = playerRepository.findByTelegramAlias(idOrAlias);
            if (temp == null) throw new NotFoundException("Player with alias " + idOrAlias + " was not found");
            id = temp.getId();
        }

        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player was not found"));

        List<Match> allMatches = player.getMatches();
        allMatches.sort(
            (m1, m2) -> m1.getTournament().getEndDate().isBefore(m2.getTournament().getEndDate()) ? -1 :
                m1.getTournament().getEndDate().isEqual(m2.getTournament().getEndDate()) &&
                    m1.getTournament().getId().compareTo(m2.getTournament().getId()) < 0 ? -1 :
                    m1.getTournament().getId().compareTo(m2.getTournament().getId()) == 0 ? 0 : 1
        );

        List<MatchWithOpponent> matches = allMatches.stream().map(m -> {
            final String opponentName;
            final float opponentRatingBefore;
            final float delta;

            if (player.equals(m.getFirstPlayer())) {
                opponentName = m.getSecondPlayer().getName();
                opponentRatingBefore = m.getSecondPlayerRatingBefore();
                delta = m.getFirstPlayerRatingDelta();
            } else {
                opponentName = m.getFirstPlayer().getName();
                opponentRatingBefore = m.getFirstPlayerRatingBefore();
                delta = m.getSecondPlayerRatingDelta();
            }

            final String score;
            if (m.getWinner().equals(player)) {
                score = m.getWinnerScore() + ":" + m.getLoserScore();
            } else score = m.getLoserScore() + ":" + m.getWinnerScore();

            return new MatchWithOpponent(opponentName, opponentRatingBefore, delta,
                score, Util.getStringFromLocalDate(m.getDate()), m.getTournament().getTitle());
        }).toList();


        final float[] rating = new float[1];
        if (allMatches.isEmpty()) rating[0] = player.getRating();
        else {
            if (allMatches.get(0).getFirstPlayer().equals(player))
                rating[0] = allMatches.get(0).getFirstPlayerRatingBefore();
            else rating[0] = allMatches.get(0).getSecondPlayerRatingBefore();
        }

        List<PlayerGraphPoint> graph = allMatches.stream().collect(Collectors
            .groupingBy(Match::getTournament)
        ).entrySet().stream().sorted((e1, e2) -> {
            Tournament t1 = e1.getKey();
            Tournament t2 = e2.getKey();
            return t1.getEndDate().isBefore(t2.getEndDate()) ? -1 :
                t1.getEndDate().isEqual(t2.getEndDate()) &&
                    t1.getId().compareTo(t2.getId()) < 0 ? -1 :
                    t1.getId().compareTo(t2.getId()) == 0 ? 0 : 1;
        }).map(e -> {
                float delta = e.getValue().stream().map(m -> {
                    if (m.getFirstPlayer().equals(player)) return m.getFirstPlayerRatingDelta();
                    else return m.getSecondPlayerRatingDelta();
                }).reduce(Float::sum).orElse(0f);
                rating[0] += delta;
                return new PlayerGraphPoint(
                    Util.getStringFromLocalDate(e.getKey().getEndDate()),
                    rating[0],
                    e.getKey().getTitle(),
                    delta
                );
            }
        ).toList();


        PLayerDto dto = new PLayerDto(player.getName(), player.getTelegramAlias(),
            statisticsService.getStatsByPlayerId(id),
            matches, graph);

        return ResponseEntity.ok(ApiResponse
            .<PLayerDto>builder()
            .status("success")
            .message("Return a player with id " + id)
            .data(dto)
            .build());
    }
}

package innopolis.tabletennis.controller;

import innopolis.tabletennis.dto.*;
import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.exception.*;
import innopolis.tabletennis.responses.*;
import innopolis.tabletennis.service.*;
import innopolis.tabletennis.util.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.util.*;
import java.util.stream.*;

@Controller
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentRESTController {
    private final TournamentService tournamentService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TournamentDto>> getAllTournaments(@RequestParam(name = "page", required = false) Integer page,
                                                                 @RequestParam(name = "size", required = false) Integer size,
                                                                 @RequestParam(name = "minPlayers", required = false, defaultValue = "0") int minPlayers,
                                                                 @RequestParam(name = "maxPlayers", required = false, defaultValue = "1000000") int maxPlayers,
                                                                 @RequestParam(name = "titleHas", required = false, defaultValue = "") String titleHas,
                                                                 @RequestParam(name = "minEndDate", required = false, defaultValue = "01.01.1970") String minEndDate,
                                                                 @RequestParam(name = "maxEndDate", required = false, defaultValue = "01.01.2970") String maxEndDate,
                                                                 @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
                                                                 @RequestParam(name = "descending", required = false, defaultValue = "true") boolean descending,
                                                                 HttpServletResponse response) {

        if (page != null) // need to prepare page
            page -= 1; // the page starts from 1 for client
        else page = 0; // first page by default is returned if not specified

        if (size == null) // size is not specified
            size = 10; // default size

        Page<Tournament> tournamentPage = tournamentService.getAllTournaments(page, size, minPlayers, maxPlayers, titleHas,
                Util.getLocalDateFromString(minEndDate), Util.getLocalDateFromString(maxEndDate), sortBy, descending);
        List<TournamentDto> dtoList = tournamentPage.getContent()
                .stream().map(TournamentDto::from).collect(Collectors.toList());
        int totalPages = tournamentPage.getTotalPages();
        response.setHeader("X-Total-Pages", String.valueOf(totalPages));
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{idOrTitle}")
    @ResponseBody
    public ResponseEntity<ApiResponse<FullTournamentDto>> getTournamentByIdOrTitle(@PathVariable String idOrTitle) {
        idOrTitle = idOrTitle.replace('-', ' ');
        Integer id;
        try {
            id = Integer.parseInt(idOrTitle);
        } catch (NumberFormatException e) {
            String finalIdOrTitle = idOrTitle;
            Tournament temp = tournamentService.getTournamentByTitle(idOrTitle)
                    .orElseThrow(() -> new NotFoundException("Tournament with alias " + finalIdOrTitle + " was not found"));
            id = temp.getId();
        }

        Optional<Tournament> optionalTournament = tournamentService.getTournamentById(id);
        Integer finalId = id;
        return optionalTournament.map(
                        tournament -> ResponseEntity.ok(ApiResponse
                                .<FullTournamentDto>builder()
                                .status("success")
                                .message("Return a tournament with id " + finalId)
                                .data(FullTournamentDto.from(tournament))
                                .build())
                )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new Tournament
    @PostMapping
    @ResponseBody
    public ResponseEntity<TournamentDto> createTournament(@Valid @RequestBody TournamentDto tournamentDto) {
        TournamentDto newTournamentDto = tournamentService.saveTournament(tournamentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTournamentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id) {
        boolean deleted = tournamentService.deleteTournamentById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> finaliseTournament(@PathVariable Integer id,
                                                   @RequestParam(required = false, defaultValue = "false") boolean forced) {
        boolean finished = tournamentService.finaliseTournament(id, forced);
        if (finished) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTournament(@PathVariable Integer id, @Valid @RequestBody TournamentDto tournamentDto) {
        tournamentService.updateTournament(id, tournamentDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<Void> updateTournamentState(@PathVariable Integer id, @RequestBody FullTournamentDto tournamentDto) {
        String newState = tournamentDto.getState();

        if (newState == null) throw new BadRequestException("New state of the tournament is not specified");

        tournamentService.updateTournamentState(id, newState);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package innopolis.tabletennis.controller;

import innopolis.tabletennis.dto.*;
import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.exception.*;
import innopolis.tabletennis.service.*;
import innopolis.tabletennis.util.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchRESTController {

    private final MatchService matchService;

    @GetMapping
    public List<MatchDto> getMatches(@RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "size", required = false) Integer size,
                                     @RequestParam(name = "startDate", required = false) String startDateString,
                                     @RequestParam(name = "endDate", required = false) String endDateString,
                                     @RequestParam(name = "hasPlayerWith", required = false, defaultValue = "") String hasPlayerWith,
                                     @RequestParam(name = "score", required = false) String score,
                                     @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
                                     @RequestParam(name = "descending", required = false, defaultValue = "true") boolean descending,
                                     HttpServletResponse response) {
        // Default sorting
        Sort sortByDateAndId = Sort.by("tournament_startDate").descending()
                .and(Sort.by("tournament_endDate").descending())
                .and(Sort.by("tournament_id").descending())
                .and(Sort.by("date").descending())
                .and(Sort.by("id").descending());

        if (page != null) // need to prepare page
            page -= 1; // the page starts from 1 for client
        else page = 0; // first page by default is returned if not specified

        if (size == null) // size is not specified
            size = 10; // default size

        Pageable pageable = PageRequest.of(page, size, sortByDateAndId);

        LocalDate startDate = null, endDate = null;
        if (startDateString != null) // start date is specified
            startDate = Util.getLocalDateFromString(startDateString);
        if (endDateString != null) // the end date is specified
            endDate = Util.getLocalDateFromString(endDateString);

        final String nameSlice = hasPlayerWith.toLowerCase();
        List<Match> content = matchService.getAllMatchesByDateBetween(startDate, endDate).stream()
                .filter(m -> {
                    boolean scoreMatched = true;
                    int scoreWinner = m.getWinnerScore();
                    int scoreLoser = m.getLoserScore();
                    if (score != null) {
                        try {
                            int firstPart = Integer.parseInt(score.split(":")[0]);
                            int secondPart = Integer.parseInt(score.split(":")[1]);
                            scoreMatched = Math.max(firstPart, secondPart) == scoreWinner &&
                                    scoreLoser == Math.min(firstPart, secondPart);
                        } catch (Exception e) {
                            throw new RequestValidationException("Score is not specified in format x:x");
                        }
                    }
                    return scoreMatched &&
                            (m.getFirstPlayer().getName().toLowerCase().contains(nameSlice) ||
                                    m.getSecondPlayer().getName().toLowerCase().contains(nameSlice));
                })
                .collect(Collectors.toList());

        if (sortBy.equals("date")) {
            content.sort((m1, m2) -> { // reversed
                Tournament t1 = m1.getTournament();
                Tournament t2 = m2.getTournament();
                LocalDate t1Start = t1.getStartDate();
                LocalDate t1End = t1.getEndDate();
                LocalDate t2Start = t2.getStartDate();
                LocalDate t2End = t2.getEndDate();
                int t1Id = t1.getId();
                int t2Id = t2.getId();
                LocalDate m1Date = m1.getDate();
                LocalDate m2Date = m2.getDate();
                int m1Id = m1.getId();
                int m2Id = m2.getId();

                int result = t1Start.isBefore(t2Start) ? 1 : t1Start.isEqual(t2Start) ? 0 : -1;
                if (result != 0) return result;
                result = t1End.isBefore(t2End) ? 1 : t1End.isEqual(t2End) ? 0 : -1;
                if (result != 0) return result;
                result = t1Id < t2Id ? 1 : t1Id == t2Id ? 0 : -1;
                if (result != 0) return result;
                result = m1Date.isBefore(m2Date) ? 1 : m1Date.isEqual(m2Date) ? 0 : -1;
                if (result != 0) return result;
                result = m1Id < m2Id ? 1 : m1Id == m2Id ? 0 : -1;

                return result;
            });
        }
        if (!descending) Collections.reverse(content);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        Page<Match> matchPage = new PageImpl<>(content.subList(start, end), pageable, content.size());

        int totalPages = matchPage.getTotalPages();
        response.setHeader("X-Total-Pages", String.valueOf(totalPages));
        return matchPage.getContent()
                .stream().map(MatchDto::from).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<MatchDto> saveMatch(@Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.saveMatch(matchDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable("id") Integer id, @Valid @RequestBody MatchDto matchDto) {
        matchService.updateMatch(id, matchDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable("id") Integer id) {
        boolean deleted = matchService.deleteMatchById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();
    }
}

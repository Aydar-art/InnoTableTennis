package innopolis.tabletennis.service;

import innopolis.tabletennis.dto.*;
import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.exception.*;
import innopolis.tabletennis.repository.*;
import innopolis.tabletennis.util.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;

    public Page<Tournament> getAllTournaments(int page, int size, int minPlayers, int maxPlayers,
                                              String titleHas, LocalDate minEndDate, LocalDate maxEndDate,
                                              String sortBy, boolean descending) {
        Sort sort;
        if (sortBy.equals("coefficient"))
            sort = Sort.by("coefficient").descending();
        else
            sort = Sort.by("startDate").descending()
                .and(Sort.by("endDate").descending())
                .and(Sort.by("id").descending());

        if (!descending) sort = sort.ascending();

        List<Tournament> content = tournamentRepository.findAllBy(sort).stream()
            .filter(t -> {
                int n = t.getNumberOfPlayers();
                return n >= minPlayers && n <= maxPlayers && t.getTitle().toLowerCase().contains(titleHas.toLowerCase()) &&
                    (t.getEndDate().isBefore(maxEndDate) || t.getEndDate().isEqual(maxEndDate)) &&
                    (t.getEndDate().isAfter(minEndDate) || t.getEndDate().isEqual(minEndDate));
            })
            .collect(Collectors.toList());

        if (sortBy.equals("players")) {
            content.sort(Comparator.comparingInt(Tournament::getNumberOfPlayers));
            if (descending) Collections.reverse(content);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }

    public Optional<Tournament> getTournamentById(Integer id) {
        return tournamentRepository.findById(id);
    }

    public Optional<Tournament> getTournamentByTitle(String title) {
        return tournamentRepository.findByTitle(title);
    }

    public TournamentDto saveTournament(TournamentDto tournamentDto) {
        // Check id absence
        if (tournamentDto.getId() != null)
            throw new UnsupportedOperationException("Saving the tournament with the given Id is unsupported. Please, consult the administration.");

        if (tournamentDto.getTitle() == null)
            throw new BadRequestException("The title for tournament to be saved is not provided");

        // Check date format and convert
        LocalDate startDate, endDate;
        try {
            startDate = Util.getLocalDateFromString(tournamentDto.getStartDateString());
            endDate = Util.getLocalDateFromString(tournamentDto.getEndDateString());
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Start or end date format of the tournament to be saved is wrong.");
        }

        // Check title uniqueness
        if (tournamentRepository.findByTitle(tournamentDto.getTitle()).isPresent())
            throw new AlreadyExistsException("The tournament with the given title already exists");

        Tournament tournament = Tournament.builder()
            .startDate(startDate)
            .endDate(endDate)
            .title(tournamentDto.getTitle())
            .finished(false)
            .coefficient(0f)
            .numberOfPlayers(0)
            .build();

        tournament = tournamentRepository.save(tournament);
        tournament.setMatches(Collections.emptyList());

        return TournamentDto.from(tournament);
    }

    public boolean deleteTournamentById(Integer id) {
        Optional<Tournament> tournament = tournamentRepository.findById(id);

        if (tournament.isPresent()) {
            Tournament foundTournament = tournament.get();
            if (!foundTournament.getMatches().isEmpty())
                throw new RequestValidationException("Can not delete tournament with matches in it.");
            tournamentRepository.delete(foundTournament);
            return true;
        }

        return false;
    }

    private boolean finaliseTournament(Tournament tournament) {

        Map<String, Player> players = new HashMap<>();
        List<Match> matches = tournament.getMatches();

        // Get players of tournament to calculate their overall rating
        for (Match match : matches) {
            Player firstPlayer = match.getFirstPlayer();
            Player secondPlayer = match.getSecondPlayer();
            if (!players.containsKey(firstPlayer.getName()))
                players.put(firstPlayer.getName(), firstPlayer);
            if (!players.containsKey(secondPlayer.getName()))
                players.put(secondPlayer.getName(), secondPlayer);

            // Update ratings before the match to the actual ones
            match.setFirstPlayerRatingBefore(firstPlayer.getRating());
            match.setSecondPlayerRatingBefore(secondPlayer.getRating());
        }

        float avgRating = players.values().stream().map(Player::getRating).reduce(0f, Float::sum) / players.size();
        float KT = avgRating / 2000.0f;
        KT = (float) (Math.ceil(KT * 10) / 10.0); // round to 1 decimal up

        if (players.size() == 0) KT = 0;

        for (Match match : matches) {
            Player firstPlayer = match.getFirstPlayer();
            Player secondPlayer = match.getSecondPlayer();
            // First player is winner
            if (match.getFirstPlayerScore().compareTo(match.getSecondPlayerScore()) > 0) {
                float winnerRatingDelta = calculateWinnerDelta(match.getFirstPlayerRatingBefore(), match.getSecondPlayerRatingBefore(), KT);
                float looserRatingDelta = -roundFloat(winnerRatingDelta / 2, 2);
                match.setFirstPlayerRatingDelta(winnerRatingDelta);
                match.setSecondPlayerRatingDelta(looserRatingDelta);
                firstPlayer.setRating(firstPlayer.getRating() + winnerRatingDelta);
                secondPlayer.setRating(secondPlayer.getRating() + looserRatingDelta);
            } else if (match.getFirstPlayerScore().compareTo(match.getSecondPlayerScore()) < 0) {
                float winnerRatingDelta = calculateWinnerDelta(match.getSecondPlayerRatingBefore(), match.getFirstPlayerRatingBefore(), KT);
                float looserRatingDelta = -roundFloat(winnerRatingDelta / 2, 2);
                match.setSecondPlayerRatingDelta(winnerRatingDelta);
                match.setFirstPlayerRatingDelta(looserRatingDelta);
                secondPlayer.setRating(secondPlayer.getRating() + winnerRatingDelta);
                firstPlayer.setRating(firstPlayer.getRating() + looserRatingDelta);
            } else throw new IllegalStateException("Scores in matches can not happen to be equal");

            // Round rating to 2 digits
            firstPlayer.setRating(roundFloat(firstPlayer.getRating(), 2));
            secondPlayer.setRating(roundFloat(secondPlayer.getRating(), 2));

            // Check 0 constraint
            if (firstPlayer.getRating() < 0) {
                firstPlayer.setRating(0f);
            }
            if (secondPlayer.getRating() < 0) {
                secondPlayer.setRating(0f);
            }
        }

        // Save players
        playerRepository.saveAll(players.values());

        // Save matches
        matchRepository.saveAll(matches);

        tournament.setFinished(true);
        tournament.setCoefficient(KT);
        tournamentRepository.save(tournament);

        return true;
    }

    public boolean finaliseTournament(Integer id, boolean forced) {
        Optional<Tournament> optionalTournament = tournamentRepository.findById(id);
        if (!optionalTournament.isPresent())
            return false;

        Tournament tournament = optionalTournament.get();

        if (!forced) {// Check that all previous tournaments are closed
            if (tournament.isFinished())
                throw new AlreadyExistsException("The tournament is already finished.");
            List<Tournament> allTournaments = tournamentRepository.findAll(Sort.by("startDate").ascending()
                .and(Sort.by("endDate").ascending())
                .and(Sort.by("id").ascending()));
            for (Tournament t : allTournaments) {
                if (t.getTitle().equals(tournament.getTitle()))
                    break;
                if (!t.isFinished())
                    throw new BadRequestException("You can not close the tournament while tournaments before are open. First, finish: " + t.getTitle());
            }
            return finaliseTournament(tournament);
        }

        List<Tournament> laterTournaments = tournamentRepository
            .findAllByStartDateGreaterThanEqual(
                tournament.getStartDate(),
                Sort.by("startDate").ascending()
                    .and(Sort.by("endDate").ascending()
                        .and(Sort.by("id").ascending())));


        for (Tournament t : laterTournaments) {
            if (isBefore(t, tournament))
                continue; // the t tournament is in the past
            if (!t.isFinished()) // stop before new tournaments appear
                break;
            // Reset players rating to the current one in the tournament
            Map<String, Player> players = new HashMap<>();
            List<Match> matches = t.getMatches();

            // Set players' rating to the one they have at the moment of update
            for (Match match : matches) {
                Player firstPlayer = match.getFirstPlayer();
                Player secondPlayer = match.getSecondPlayer();
                if (!players.containsKey(firstPlayer.getName())) {
                    players.put(firstPlayer.getName(), firstPlayer);
                    firstPlayer.setRating(match.getFirstPlayerRatingBefore());
                }
                if (!players.containsKey(secondPlayer.getName())) {
                    players.put(secondPlayer.getName(), secondPlayer);
                    secondPlayer.setRating(match.getSecondPlayerRatingBefore());
                }
            }
            t.setFinished(false); // force update
            finaliseTournament(t);
        }

        return true;
    }

    private static boolean isBefore(Tournament t1, Tournament t2) {
        if (t1.getStartDate().isBefore(t2.getStartDate()))
            return true;
        if (t1.getStartDate().isEqual(t2.getStartDate())) {
            if (t1.getEndDate().isBefore(t2.getEndDate()))
                return true;
            if (t1.getEndDate().isEqual(t2.getEndDate()))
                if (t1.getId() < t2.getId())
                    return true;
        }

        return false;
    }

    private static float calculateWinnerDelta(float winnerRating, float loserRating, float KT) {
        if (winnerRating - loserRating > 100)
            return 0;
        float ratingDelta;
        ratingDelta = (100 - winnerRating + loserRating) * KT / 10;
        return roundFloat(ratingDelta, 2);
    }

    private static float roundFloat(float value, int places) {
        if (places < 0) throw new IllegalArgumentException("Places after 0. must be > 0");

        long factor = (long) Math.pow(10, places);
        value = value * factor; // upcast
        long tmp = Math.round(value); // round
        return tmp * 1f / factor; // downcast
    }

    public void updateTournament(Integer id, TournamentDto tournamentDto) {
        if (tournamentDto.getId() != null)
            throw new UnsupportedOperationException("Saving the tournament with the given Id is unsupported. Please, consult the administration.");
        if (tournamentDto.getTitle() == null)
            throw new BadRequestException("The title for tournament to be updated is not provided");

        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException("Tournament to be updated with id " + id + " is not found"));
        if (!tournament.getTitle().equals(tournamentDto.getTitle()) &&
            tournamentRepository.findByTitle(tournamentDto.getTitle()).isPresent()) {
            throw new IllegalArgumentException("The new tournament title must be unique");
        }
        tournament.setTitle(tournamentDto.getTitle());

        LocalDate newStartDate = Util.getLocalDateFromString(tournamentDto.getStartDateString());
        LocalDate newEndDate = Util.getLocalDateFromString(tournamentDto.getEndDateString());

        // Check that all matches align with the new tournament time interval;
        for (Match m : tournament.getMatches())
            if (newStartDate.isAfter(m.getDate()) || newEndDate.isBefore(m.getDate()))
                throw new RequestValidationException("New tournament dates exclude some matches");

        tournament.setStartDate(newStartDate);
        tournament.setEndDate(newEndDate);
        tournamentRepository.save(tournament);
    }

    public void updateTournamentState(Integer id, String newState) {
        Tournament t = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException("Tournament was not found"));
        t.setState(newState);
        tournamentRepository.save(t);
    }
}

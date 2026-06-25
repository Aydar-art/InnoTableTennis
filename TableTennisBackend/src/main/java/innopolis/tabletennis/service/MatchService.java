package innopolis.tabletennis.service;

import innopolis.tabletennis.dto.*;
import innopolis.tabletennis.entity.*;
import innopolis.tabletennis.exception.*;
import innopolis.tabletennis.repository.*;
import innopolis.tabletennis.util.*;
import lombok.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;

    public List<Match> getAllMatchesByDateBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.of(1970, 1, 1); // start from the very beginning
        if (endDate == null) endDate = LocalDate.of(3000, 1, 1); // the very last day

        return matchRepository.findAllByDateBetween(startDate, endDate);
    }

    public boolean deleteMatchById(Integer id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isPresent()) {
            matchRepository.delete(match.get());
            return true;
        }
        return false;
    }

    public MatchDto saveMatch(MatchDto matchDto) {
        if (matchDto.getFirstPlayerName().equals(matchDto.getSecondPlayerName()))
            throw new RequestValidationException("Players` names in match must be different");
        if (matchDto.getFirstPlayerScore().equals(matchDto.getSecondPlayerScore()))
            throw new RequestValidationException("Scores of match players must be different");


        Player firstPlayer = playerRepository.findByName(matchDto.getFirstPlayerName());
        Player secondPlayer = playerRepository.findByName(matchDto.getSecondPlayerName());
        if (firstPlayer == null || secondPlayer == null)
            throw new RequestValidationException("No players with such names were found");

        Tournament tournament = tournamentRepository.findByTitle(matchDto.getTournamentTitle()).orElse(null);
        if (tournament == null || tournament.isFinished())
            throw new RequestValidationException("No tournament with such title was found or it is finished");


        Match match = Match.from(matchDto);
        if (tournament.getStartDate().isAfter(match.getDate()) || tournament.getEndDate().isBefore(match.getDate()))
            throw new RequestValidationException("The match date must be within the tournament`s start and end dates");

        match.setTournament(tournament);
        match.setFirstPlayer(firstPlayer);
        match.setSecondPlayer(secondPlayer);

        match.setFirstPlayerRatingBefore(firstPlayer.getRating());
        match.setSecondPlayerRatingBefore(secondPlayer.getRating());

        // To be calculated after the tournament is finished
        match.setFirstPlayerRatingDelta(0f);
        match.setSecondPlayerRatingDelta(0f);

        // Calculate rating delta
        if (matchDto.getFirstPlayerScore().compareTo(matchDto.getSecondPlayerScore()) > 0) {
            firstPlayer.setNumberOfWins(firstPlayer.getNumberOfWins() + 1);
            secondPlayer.setNumberOfLosses(secondPlayer.getNumberOfLosses() + 1);
        } else if (matchDto.getFirstPlayerScore().compareTo(matchDto.getSecondPlayerScore()) < 0) {
            firstPlayer.setNumberOfLosses(firstPlayer.getNumberOfLosses() + 1);
            secondPlayer.setNumberOfWins(secondPlayer.getNumberOfWins() + 1);
        } else throw new IllegalStateException("Scores in matches can not happen to be equal");

        // Save players
        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);

        return MatchDto.from(matchRepository.save(match));
    }

    public void updateMatch(Integer id, MatchDto matchDto) {
        if (matchDto.getFirstPlayerName().equals(matchDto.getSecondPlayerName()))
            throw new RequestValidationException("Players` names in match must be different");
        if (matchDto.getFirstPlayerScore().equals(matchDto.getSecondPlayerScore()))
            throw new RequestValidationException("Scores of match players must be different");

        Match match = matchRepository.findById(id).orElseThrow(() -> new RequestValidationException("Match with id " + id + " was not found"));
        Tournament tournament = match.getTournament();
        if (tournament.isFinished())
            throw new RequestValidationException("Can't update match of the finished tournament");

        // Move to a different tournament
        if (!matchDto.getTournamentTitle().equals(tournament.getTitle())) {
            Tournament t = tournamentRepository.findByTitle(matchDto.getTournamentTitle()).orElseThrow(() -> new RequestValidationException("Tournament with title " + matchDto.getTournamentTitle() + " was not found"));
            if (t.isFinished()) throw new RequestValidationException("The new tournament is finished!");
            match.setTournament(t);
        }

        LocalDate newDate = Util.getLocalDateFromString(matchDto.getLocalDateString());
        match.setDate(newDate);
        if (match.getTournament().getStartDate().isAfter(newDate) || match.getTournament().getEndDate().isBefore(newDate))
            throw new RequestValidationException("The match date must be within the tournament`s start and end dates");

        Player previousFirst = match.getFirstPlayer();
        Player previousSecond = match.getSecondPlayer();

        // Recalculate win rates of previous players
        if (match.getFirstPlayerScore().compareTo(match.getSecondPlayerScore()) > 0) {
            previousFirst.setNumberOfWins(previousFirst.getNumberOfWins() - 1);
            previousSecond.setNumberOfLosses(previousSecond.getNumberOfLosses() - 1);
        } else if (match.getFirstPlayerScore().compareTo(match.getSecondPlayerScore()) < 0) {
            previousFirst.setNumberOfLosses(previousFirst.getNumberOfLosses() - 1);
            previousSecond.setNumberOfWins(previousSecond.getNumberOfWins() - 1);
        } else throw new IllegalStateException("Scores in matches can not happen to be equal");

        Player first = playerRepository.findByName(matchDto.getFirstPlayerName());
        Player second = playerRepository.findByName(matchDto.getSecondPlayerName());
        if (first == null || second == null) throw new RequestValidationException("New players were not found");

        match.setFirstPlayer(first);
        match.setSecondPlayer(second);
        match.setFirstPlayerRatingBefore(first.getRating());
        match.setSecondPlayerRatingBefore(second.getRating());
        match.setFirstPlayerScore(matchDto.getFirstPlayerScore());
        match.setSecondPlayerScore(matchDto.getSecondPlayerScore());

        // Recalculate win rates
        if (matchDto.getFirstPlayerScore().compareTo(matchDto.getSecondPlayerScore()) > 0) {
            first.setNumberOfWins(first.getNumberOfWins() + 1);
            second.setNumberOfLosses(second.getNumberOfLosses() + 1);
        } else if (matchDto.getFirstPlayerScore().compareTo(matchDto.getSecondPlayerScore()) < 0) {
            first.setNumberOfLosses(first.getNumberOfLosses() + 1);
            second.setNumberOfWins(second.getNumberOfWins() + 1);
        } else throw new IllegalStateException("Scores in matches can not happen to be equal");

        playerRepository.save(previousFirst);
        playerRepository.save(previousSecond);
        playerRepository.save(first);
        playerRepository.save(second);

        matchRepository.save(match);
    }
}

package innopolis.tabletennis.controller;

import innopolis.tabletennis.dto.*;
import innopolis.tabletennis.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.*;

import javax.validation.*;
import java.net.*;
import java.util.*;

@RestController
@RequestMapping("/api/leaders")
@RequiredArgsConstructor
public class LeaderController {
    private final LeaderService leaderService;

    @GetMapping
    public ResponseEntity<List<LeaderDto>> getAllLeaders() {
        return ResponseEntity.ok(leaderService.findAllLeaders());
    }

    @PostMapping
    public ResponseEntity<LeaderDto> addLeader(@Valid @RequestBody LeaderDto leaderDto) {
        LeaderDto newLeaderDto = leaderService.addLeader(leaderDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLeaderDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(newLeaderDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeader(@PathVariable Integer id) {
        if (leaderService.deleteLeader(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

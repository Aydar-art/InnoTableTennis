package innopolis.tabletennis.telegram;

import innopolis.tabletennis.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramAuthController {
    private final AuthenticationService service;

    @PostMapping("/auth")
    public ResponseEntity<TelegramAuthResponse> authenticate(final @RequestBody TelegramUser user) {
        log.info("The user[{}] {} requested a password", user.getId(), user.getUserName());

        final String password = service.resetPassword(user.getUserName());

        if (password == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(TelegramAuthResponse.builder().password(password).build());
    }
}

package innopolis.tabletennis.controller;

import innopolis.tabletennis.dto.auth.*;
import innopolis.tabletennis.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationRESTController {
    private final AuthenticationService authService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authRequest,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        // auth without password
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return ResponseEntity.ok(authService.authenticateByUsername(authRequest.getUsername()));

        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<AuthenticationResponse> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(authService.changePassword(changePasswordRequest));
    }

    @PostMapping("/roles")
    public ResponseEntity<String> changeRoles(@Valid @RequestBody AddRoleRequest addRoleRequest) {
        return ResponseEntity.ok(authService.addRoleToUser(addRoleRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok().body(authService.getAllUsersAndRoles());
    }

    @DeleteMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(name = "username") String username) {
        boolean deleted = authService.deleteUserByUsername(username);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();
    }

}

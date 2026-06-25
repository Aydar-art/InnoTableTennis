package innopolis.tabletennis.service;

import innopolis.tabletennis.dto.auth.*;
import innopolis.tabletennis.entity.Role;
import innopolis.tabletennis.entity.User;
import innopolis.tabletennis.exception.AlreadyExistsException;
import innopolis.tabletennis.exception.NotFoundException;
import innopolis.tabletennis.repository.PlayerRepository;
import innopolis.tabletennis.repository.RoleRepository;
import innopolis.tabletennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final String ROLE_PREFIX = "ROLE_";
    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            Set<Role> roles = new HashSet<>();
            roles.add(new Role("ROLE_USER"));
            roles.add(new Role("ROLE_LEADER"));
            roles.add(new Role("ROLE_ADMIN"));
            User user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(roles)
                .build();
            userRepository.save(user);
        }
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent())
            throw new AlreadyExistsException("User already exists");

        Role userRole = roleRepository.findByName(ROLE_PREFIX + "USER")
            .orElseThrow(() -> new UsernameNotFoundException("User Role not found"));

        User user = User.builder()
            .username(registerRequest.getUsername())
            .roles(new HashSet<>())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .build();

        userRepository.save(user);

        // Add userRole to a new saved user
        user = userRepository.findByUsername(registerRequest.getUsername()).orElseThrow(() -> new IllegalStateException("User was not saved properly"));
        user.getRoles().add(userRole);
        userRepository.save(user);

        Map<String, Object> roleClaims = Collections.singletonMap("roles",
            user.getRoles().parallelStream().map(role -> role.getName().substring(ROLE_PREFIX.length())).toList());

        return new AuthenticationResponse(jwtService.generateToken(roleClaims, user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword())
        );

        return authenticateByUsername(authRequest.getUsername());
    }

    public AuthenticationResponse authenticateByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Object> roleClaims = Collections.singletonMap("roles",
            user.getRoles().parallelStream().map(
                role -> role.getName().substring(ROLE_PREFIX.length())
            ).toList());

        return new AuthenticationResponse(jwtService.generateToken(roleClaims, user));
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        if (changePasswordRequest.getOldPassword() == null || changePasswordRequest.getNewPassword() == null)
            throw new IllegalArgumentException("Passwords were not specified");
        if (changePasswordRequest.getUsername() == null)
            throw new IllegalArgumentException("Username was not specified");

        Optional<User> userOptional = userRepository.findByUsername(changePasswordRequest.getUsername());
        if (!userOptional.isPresent())
            throw new IllegalArgumentException("User not found");
        User user = userOptional.get();

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword()))
            throw new IllegalArgumentException("Password is incorrect");

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public String addRoleToUser(AddRoleRequest addRoleRequest) {
        if (addRoleRequest.getUsername() == null || addRoleRequest.getRole() == null)
            throw new IllegalArgumentException("Username or role was not specified");


        User user = userRepository.findByUsername(addRoleRequest.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(ROLE_PREFIX + addRoleRequest.getRole().toUpperCase())
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if (role.getName().toUpperCase().contains("ADMIN"))
            throw new IllegalArgumentException("Can not add ADMIN role");

        user.getRoles().add(role);
        userRepository.save(user);

        return addRoleRequest.getRole();
    }

    public String addRoleToUser(String username, String roleName) {
        return addRoleToUser(new AddRoleRequest(username, roleName));
    }

    public List<UserDto> getAllUsersAndRoles() {
        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

    public boolean deleteUserByUsername(String username) {
        if (username == null)
            return false;
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent())
            return false;
        userRepository.delete(userOptional.get());
        return true;
    }

    public String registerByUsername(String username) {
        if (username == null)
            return null;
        // return password or null if not successful
        if (playerRepository.findByTelegramAlias(username) == null) return null;
        if (userRepository.findByUsername(username).isPresent()) return null;

        String password = String.valueOf(System.currentTimeMillis());

        RegisterRequest req = RegisterRequest.builder().username(username).password(password)
            .build();

        register(req);

        return password;
    }

    /**
     * @return password of registered user
     */
    public String registerByUsernameWithChatId(String username, Long userTelegramChatId) {
        if (userTelegramChatId == null) {
            return null;
        }
        String password = registerByUsername(username);

        // Should check that chatId exists to update the player alias as well. Since newAlias is not registered because player has an old one

        // Add chatId to a registered user
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("It's not possible that User is not saved after registering"));
        if (user.getTelegramChatId() == null) {
            user.setTelegramChatId(userTelegramChatId);
            userRepository.save(user);
        }

        return password;
    }

    /**
     * Username - user login
     * Returns null if username was not found
     * else returns the newly generated password storing it
     */
    public String resetPassword(@NotNull String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        String password = String.valueOf(System.currentTimeMillis());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return password;
    }

    public void changeUsername(String oldAlias, String newAlias) {
        User user = userRepository.findByUsername(oldAlias).orElseThrow(() -> new NotFoundException("User with alias " + oldAlias + " was not found"));
        user.setUsername(newAlias);
        userRepository.save(user);
    }

    public boolean deleteRole(String roleName, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(
            "User with username " + username + " is not found")
        );
        user.getRoles().remove(new Role(ROLE_PREFIX + roleName));
        userRepository.save(user);
        return true;
    }

    /**
     * Register a username if not exists. Update the username of a user if exists.
     */
    public void saveOrUpdateUsername(Long telegramChatId, String username) {
        if (telegramChatId == null || username == null)
            throw new IllegalArgumentException("Telegram chat id and username must be not null");

        final Optional<User> optionalUser = userRepository.findByTelegramChatId(telegramChatId);

        if (optionalUser.isPresent()) { // Update username for future log in
            final User user = optionalUser.get();
            user.setUsername(username);
            userRepository.save(user);
        } else {
            registerByUsernameWithChatId(username, telegramChatId);
        }
    }
}

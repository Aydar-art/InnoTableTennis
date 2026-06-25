package innopolis.tabletennis.dto.auth;

import innopolis.tabletennis.entity.*;
import lombok.*;

import java.util.*;
import java.util.stream.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private List<String> roles;

    public static UserDto from(User user) {
        Set<Role> roles = user.getRoles();
        List<String> roleTitles = roles.stream().map(Role::getName).collect(Collectors.toList());
        String username = user.getUsername();
        return UserDto.builder().username(username).roles(roleTitles).build();
    }
}

package innopolis.tabletennis.config;

import innopolis.tabletennis.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(requests -> requests
                .antMatchers(HttpMethod.POST, "/api/telegram/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/*.css", "/*.png", "/_app/**", "/players", "/", "/login", "/tournaments", "/signup").permitAll() // static resources
                .antMatchers(HttpMethod.GET, "/api/matches").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/authenticate").permitAll()
                .antMatchers(HttpMethod.GET, "/api/players", "/api/tournaments").hasAnyRole("USER", "LEADER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/auth/changePassword").hasAnyRole("USER", "LEADER", "ADMIN")
                .antMatchers("/api/matches/**", "/api/players/**", "/api/tournaments/**", "/api/leaders/**", "/auth/roles", "/auth/register").hasAnyRole("LEADER", "ADMIN")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}


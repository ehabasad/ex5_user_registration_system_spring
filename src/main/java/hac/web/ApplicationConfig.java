package hac.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ApplicationConfig  {

    private final InMemoryUserDetailsManager manager;

    public ApplicationConfig() {
        this.manager = new InMemoryUserDetailsManager();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
        manager.createUser(User.withUsername("admin")
                .password(bCryptPasswordEncoder.encode("password"))
                .roles("ADMIN")
                .build());
        return manager;
    }

    public void addUser(String username, String password) {
        String encodedPassword = passwordEncoder().encode(password);
        UserDetails user = User
                                .withUsername(username)
                                .password(encodedPassword)
                                .roles("USER")
                                .build();
        manager.createUser(user);
    }

    public void deleteUser(String username) {
        manager.deleteUser(username);
    }

    public void disableUser(String username) {
        UserDetails user = manager.loadUserByUsername(username);
        User updatedUser = (User) User.withUserDetails(user)
                .disabled(true)
                .build();
        manager.updateUser(updatedUser);
    }

    public void enableUser(String username) {
        UserDetails user = manager.loadUserByUsername(username);
        User updatedUser = (User) User.withUserDetails(user)
                .disabled(false)
                .build();
        manager.updateUser(updatedUser);
    }


    public void changePassword(String username, String newPassword) {
        String encodedPassword = passwordEncoder().encode(newPassword);
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .roles("USER")
                .build();
        manager.updateUser(user);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(withDefaults())

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/", "/403" , "/errorpage" , "/simulateError" , "/users/register",
                                "/password-reset" , "/new-password").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/profile").hasRole("USER")
                )
                .formLogin((form) -> form.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
//                                .failureUrl("/")
                                .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                                .accessDeniedPage("/403")
                )

        ;

        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
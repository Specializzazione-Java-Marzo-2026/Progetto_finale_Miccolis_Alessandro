package it.aulab.progetto_finale_java.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


private final UserDetailsService userDetailsService;

public SecurityConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorize -> authorize
                    // rotte pubbliche
                    .requestMatchers(
                            "/", "/home", "/login", "/register", "/search",
                            "/css/**", "/js/**",
                            "/article/*", "/category/**", "/author/**"
                    ).permitAll()

                    // richiesta collaborazione: utente autenticato
                    .requestMatchers("/operations/career/**").authenticated()

                    // area admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")

                    // area revisore
                    .requestMatchers("/revisor/**").hasRole("REVISOR")

                    // area writer
                    .requestMatchers(
                            "/writer/**",
                            "/article/create",
                            "/article/store",
                            "/article/edit/**",
                            "/article/update/**",
                            "/article/delete/**"
                    ).hasRole("WRITER")

                    // tutto il resto richiede autenticazione
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            )
            .userDetailsService(userDetailsService);

    return http.build();
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}


}

package com.microdiab.mpatient.configurations;

import com.microdiab.mpatient.filter.LoggingFilter;
import com.microdiab.mpatient.filter.RequestLoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for security settings in the mPatient microservice.
 * This class defines the security filter chain and user details service,
 * enabling HTTP basic authentication and role-based access control.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private LoggingFilter loggingFilter;


    /**
     * Configures the security filter chain for HTTP requests.
     * Permits public access to actuator, Swagger UI, and related endpoints.
     * Restricts access to all other endpoints to users with the "INTERNAL" role.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/**",
                                "/apidocs/**",
                                "/swagger*/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/favicon.ico"
                            ).permitAll()
                        .requestMatchers("/**").hasRole("INTERNAL")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(new RequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * Configures an in-memory user details service with a default "INTERNAL" user.
     * The password is encoded using Spring Security's delegating password encoder.
     *
     * @return the configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails patientUser = User.withUsername("username")
                .password(encoder.encode("user"))
                .roles("INTERNAL")
                .build();
        return new InMemoryUserDetailsManager(patientUser);
    }

}

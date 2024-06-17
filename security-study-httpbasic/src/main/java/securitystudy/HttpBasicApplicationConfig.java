package securitystudy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
@EnableWebSecurity
public class HttpBasicApplicationConfig {
    @Bean
    protected SecurityFilterChain configureHttpBasic(final HttpSecurity http) throws Exception {
        http.cors(CorsConfigurer::disable);
        http.csrf(CsrfConfigurer::disable);
        http.headers(HeadersConfigurer::disable);

        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/login")
                        .anonymous()
                        .requestMatchers("/api/protected")
                        .authenticated()
                        .anyRequest()
                        .permitAll());

        http.httpBasic(Customizer.withDefaults());

        http.logout(logout ->
                logout.logoutSuccessUrl("/?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));


        http.exceptionHandling(configure ->
                configure.authenticationEntryPoint(new Http403ForbiddenEntryPoint()));
        return http.build();
    }

    @Bean
    protected InMemoryUserDetailsManager userDetailsHttpBasicService() {
        final var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        final UserDetails user = User.builder()
                .username("user@naver.com")
                .password("password1234")
                .passwordEncoder(encoder::encode)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

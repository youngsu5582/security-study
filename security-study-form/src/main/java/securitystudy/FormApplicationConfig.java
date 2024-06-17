package securitystudy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class FormApplicationConfig {
    private static final String LOGIN_URI = "/login";
    @Bean
    protected SecurityFilterChain configureForm(final HttpSecurity http) throws Exception {
        http.cors(CorsConfigurer::disable);
        http.csrf(CsrfConfigurer::disable);
        http.headers(HeadersConfigurer::disable);

        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers(LOGIN_URI)
                        .anonymous()
                        .requestMatchers("/api/protected")
                        .authenticated()
                        .anyRequest()
                        .permitAll());

        http.formLogin(form ->
                form.loginPage(LOGIN_URI)
                        .loginProcessingUrl(LOGIN_URI)
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
        );

        http.logout(logout ->
                logout.logoutSuccessUrl("/?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));


        http.exceptionHandling(configure ->
                configure.authenticationEntryPoint(new Http403ForbiddenEntryPoint()));
        return http.build();
    }

    @Bean
    protected InMemoryUserDetailsManager userDetailsFormService() {
        final var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        final UserDetails user = User.builder()
                .passwordEncoder(encoder::encode)
                .username("user@naver.com")
                .password("password1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

package org.example.securitystudy.authentication;

import org.example.securitystudy.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class AuthenticationManagerTest {
    /**
     * Configuration은 자동으로 Context에서 필요한 요소들을 통해 구성한다.
     * 아래, UserDetailsService, PasswordEncoder 들 자동으로 포함
     */
    @Autowired
    AuthenticationConfiguration configuration;
    @Autowired
    UserPrincipalDetailService userPrincipalDetailService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoderConfig passwordEncoderConfig;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void Authentication을_통해_인증을_완료한다() throws Exception {
        final String encoded = passwordEncoder.encode("password1234");
        final User user = new User("username", encoded);
        userRepository.save(user);
        final AuthenticationManager authenticationManager = configuration.getAuthenticationManager();

        final UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
                .unauthenticated("username", "password1234");

        final Authentication authentication = authenticationManager.authenticate(token);

        assertThat(authentication).isNotSameAs(token);
        assertThat(authentication.getPrincipal()).isEqualTo(new UserPrincipal(user));
        assertThat(authentication.getCredentials()).isNull();
    }

    @Test
    void 인증에_실패하면_예외를_발생한다() throws Exception {
        final AuthenticationManager authenticationManager = configuration.getAuthenticationManager();
        final UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
                .unauthenticated("username", "password1234");

        assertThatThrownBy(()->authenticationManager.authenticate(token))
                .isInstanceOf(BadCredentialsException.class);
    }
}

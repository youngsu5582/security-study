package org.example.securitystudy.authentication;

import org.example.securitystudy.user.User;
import org.example.securitystudy.user.UserPrincipalDetailService;
import org.example.securitystudy.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 해당 테스트는 AuthenticationManager 를 먼저 보고 오는걸 추천한다.
 */
@SpringBootTest
class UsernamePasswordAuthenticationFilterTest {
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

    /**
     * 요청에서 추출한 후, AuthenticationManager 의 로직을 수행한다.
     *
     * @throws Exception
     */
    @Test
    void 요청에서_Authentication을_추출해서_인증한다() throws Exception {
        final String encoded = passwordEncoder.encode("password1234");
        final User user = new User("username", encoded);
        userRepository.save(user);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", "username");
        request.setParameter("password", "password1234");
        request.setMethod("POST");

        final UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(
                configuration.getAuthenticationManager()
        );
        final Authentication authentication = filter.attemptAuthentication(request, new MockHttpServletResponse());

        assertThat(authentication.getCredentials()).isNull();
    }
}

package org.example.securitystudy.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.example.securitystudy.user.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthenticationFilterTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final JwtProvider jwtProvider = new JwtProvider(
            "secretKeyaisamustb256bbitscoveritmeansrequireword56charsequence",
            320000L,
            64000000L,
            new UserPrincipalDetailService(userRepository)
    );
    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
        jwtProvider
    );

    @Test
    void 헤더에_토큰을_담으면_인증을_진행한다() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final User user = new User("joyson5582@gmail.com", "password1234", List.of(Role.CUSTOMER));
        Mockito.when(userRepository.findByEmail("joyson5582@gmail.com"))
                .thenReturn(Optional.of(user));


        final var token = jwtProvider.generateToken("joyson5582@gmail.com");
        request.addHeader("Authorization","Bearer "+token.accessToken());
        filter.doFilter(request, new MockHttpServletResponse(), Mockito.mock(FilterChain.class));
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isEqualTo(
                new UsernamePasswordAuthenticationToken(new UserPrincipal(user),null,
                        List.of(new SimpleGrantedAuthority(Role.CUSTOMER.name())))
        );
    }
}

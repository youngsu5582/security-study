package org.example.securitystudy.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.example.securitystudy.user.Role;
import org.example.securitystudy.user.User;
import org.example.securitystudy.user.UserPrincipalDetailService;
import org.example.securitystudy.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JwtExceptionFilterTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final JwtProvider jwtProvider = new JwtProvider(
            "secretKeyaisamustb256bbitscoveritmeansrequireword56charsequence",
            320000L,
            64000000L,
            new UserPrincipalDetailService(userRepository)
    );
    private final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
            jwtProvider
    );
    @Test
    void 예외를_받아서_응답값을_변환한다() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();

        final User user = new User("joyson5582@gmail.com", "password1234", List.of(Role.CUSTOMER));
        Mockito.when(userRepository.findByEmail("joyson5582@gmail.com"))
                .thenReturn(Optional.of(user));

        final FilterChain filterChain = new CustomFilterChain(jwtAuthenticationFilter, jwtExceptionFilter);
        jwtExceptionFilter.doFilter(request, response,filterChain);
        final ObjectMapper objectMapper = new ObjectMapper();
        final var errorResponse = objectMapper.readValue(response.getContentAsString(),ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(errorResponse.errorCode()).isEqualTo("AUTH-004");
    }
}

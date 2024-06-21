package org.example.securitystudy.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationEntryPointTest {
    @Test
    void 여러가지의_제공_EntryPoint가_존재한다(){
        final AuthenticationEntryPoint entryPoint = new Http403ForbiddenEntryPoint();
        final AuthenticationEntryPoint entryPoint1 = new BasicAuthenticationEntryPoint();
        assertThat(entryPoint).isInstanceOf(AuthenticationEntryPoint.class);
        assertThat(entryPoint1).isInstanceOf(AuthenticationEntryPoint.class);
    }
    private class CustomEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
            final ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(objectMapper.writeValueAsBytes(new ErrorResponse("AUTH-001","인증을 해야합니다.")));
        }
    }
    @Test
    void 반환_상태코드_결과를_커스텀할_수_있다() throws ServletException, IOException {
        final AuthenticationEntryPoint entryPoint = new CustomEntryPoint();
        final var response = new MockHttpServletResponse();
        entryPoint.commence(new MockHttpServletRequest(),response , null);

        final ObjectMapper objectMapper = new ObjectMapper();
        final var content = response.getContentAsString();
        final ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(errorResponse.errorCode()).isEqualTo("AUTH-001");
        assertThat(errorResponse.message()).isEqualTo("인증을 해야합니다.");

    }
}

package org.example.securitystudy.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (final ExpiredJwtException exception) {
            setErrorResponse(response, new ErrorResponse("AUTH-003","시간이 만료되었습니다."));
        } catch (final JwtException exception) {
            setErrorResponse(response, new ErrorResponse("AUTH-004","올바르지 않은 토큰입니다."));
        }
    }

    private void setErrorResponse(final HttpServletResponse response, final ErrorResponse errorResponse) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(errorResponse));
    }
}

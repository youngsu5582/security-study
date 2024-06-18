package org.example.securitystudy.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernamePasswordAuthenticationTokenTest {

    final String principal = "username";
    final String credentials = "password";

    @Test
    void pricipal과_credentials를_통해_생성한다() {
        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, credentials);
        assertThat(authentication.getPrincipal()).isEqualTo(principal);
        assertThat(authentication.getCredentials()).isEqualTo(credentials);
        assertThat(authentication.isAuthenticated()).isFalse();
    }

    @Test
    void 팩토리_메소드로_생성가능하다() {
        final Authentication unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(principal, credentials);

        final Authentication authenticated = UsernamePasswordAuthenticationToken.authenticated(
                principal, credentials, List.of(new SimpleGrantedAuthority("USER"))
        );
        assertThat(unauthenticated.isAuthenticated()).isFalse();
        assertThat(authenticated.isAuthenticated()).isTrue();
    }

    @Test
    void 인증_상태를_거짓으로_변환할수있다() {
        final Authentication authenticated = UsernamePasswordAuthenticationToken.authenticated(
                principal, credentials, List.of(new SimpleGrantedAuthority("USER")));

        authenticated.setAuthenticated(false);
        assertThat(authenticated.isAuthenticated()).isFalse();
    }
    @Test
    void 인증_상태를_참으로_변한할수없다() {
        final Authentication unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(principal, credentials);

        assertThatThrownBy(()->unauthenticated.setAuthenticated(true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

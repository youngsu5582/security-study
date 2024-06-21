package org.example.securitystudy.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.example.securitystudy.user.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class JwtProviderTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);

    // Base64 는 _ or - or ' '을 허용하지 않는다.
    JwtProvider jwtProvider = new JwtProvider(
            "secretKeyaisamustb256bbitscoveritmeansrequireword56charsequence",
            320000L,
            64000000L,
            new UserPrincipalDetailService(userRepository)
    );

    @Test
    void 문자열을_통해_토큰을_생성한다() {
        // token 의 username 을 다시 받는건 보안상 상관이 없다.
        final var token = jwtProvider.generateToken("joyson5582@gmail.com");

        assertThat(jwtProvider.getUsername(token.accessToken())).isEqualTo("joyson5582@gmail.com");
        assertThat(jwtProvider.getUsername(token.accessToken()))
                .isEqualTo(jwtProvider.getUsername(token.refreshToken()));
        assertThat(jwtProvider.getExpiration(token.accessToken()))
                .isLessThan(320000L);
    }

    @Test
    void 토큰을_통해_인증을_완료한다() {
        final Token token = jwtProvider.generateToken("joyson5582@gmail.com");
        final User user = new User("joyson5582@gmail.com", "password1234", List.of(Role.CUSTOMER));
        Mockito.when(userRepository.findByEmail("joyson5582@gmail.com"))
                .thenReturn(Optional.of(user));

        //jwtProvider 가 인증을 완료한 후 Authentication 을 넘겨준다.
        final var result = jwtProvider.getAuthentication(token.accessToken());

        assertThat(result).isEqualTo(
                new UsernamePasswordAuthenticationToken(new UserPrincipal(user),null,
                        List.of(new SimpleGrantedAuthority(Role.CUSTOMER.name())))
        );
    }
    @Test
    void 토큰_시간이_만료되면_예외를_발생한다(){
        final JwtProvider expireJwtProvider = new JwtProvider(
                "secretKeyaisamustb256bbitscoveritmeansrequireword56charsequence",
                50L,
                64000000L,
                new UserPrincipalDetailService(userRepository)
        );
        final var token = expireJwtProvider.generateToken("joyson5582@gmail.com");

        assertThatThrownBy(()->expireJwtProvider.validateToken(token.accessToken()))
                .isInstanceOf(ExpiredJwtException.class);
        assertThatCode(()->expireJwtProvider.validateToken(token.refreshToken()))
                .doesNotThrowAnyException();
    }
    @Test
    void 시크릿키가_다르면_예외를_발생한다(){
        final JwtProvider newProvider = new JwtProvider(
                // 맨뒤 e->s로 변경해도 바로 예외 발생
                "secretKeyaisamustb256bbitscoveritmeansrequireword56charsequencs",
                50000L,
                64000000L,
                new UserPrincipalDetailService(userRepository)
        );
        final var token = jwtProvider.generateToken("joyson5582@gmail.com");

        assertThatThrownBy(()->newProvider.validateToken(token.accessToken()))
                .isInstanceOf(JwtException.class);
    }
}

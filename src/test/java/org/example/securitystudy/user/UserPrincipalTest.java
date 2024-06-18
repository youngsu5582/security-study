package org.example.securitystudy.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserPrincipalTest {
    @Test
    @DisplayName("엔티티를 프록시 패턴을 사용해 UserPrincipal 로 생성한다.")
    void some2(){
        final User user = new User("i894@naver.com","password1234");
        final UserPrincipal userPrincipal = new UserPrincipal(user);
        assertThat(userPrincipal.getUsername()).isEqualTo("i894@naver.com");
        assertThat(userPrincipal.getPassword()).isEqualTo("password1234");
        assertThat(userPrincipal.getAuthorities()).hasSize(1)
                .anyMatch(authority -> authority.getAuthority().equals("CUSTOMER"));
    }
    @Test
    @DisplayName("계정이 만료되었는지 확인한다.")
    void some(){
        final User user = new User("i894@naver.com","password1234");
        final UserPrincipal userPrincipal = new UserPrincipal(user);
        assertThat(userPrincipal.isAccountNonExpired()).isFalse();
        user.expired();
        assertThat(userPrincipal.isAccountNonExpired()).isTrue();
    }
}

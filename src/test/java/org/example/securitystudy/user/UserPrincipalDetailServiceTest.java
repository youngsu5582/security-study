package org.example.securitystudy.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
class UserPrincipalDetailServiceTest {
    @Autowired
    UserRepository userRepository;
    @Test
    void 사용자가_있으면_UserDetail로_반환한다(){
        final User user = new User("joyson5582@gmail.com", "password1234");
        userRepository.save(user);
        final UserPrincipalDetailService userPrincipalService = new UserPrincipalDetailService(userRepository);

        final var userPrincipal = userPrincipalService.loadUserByUsername("joyson5582@gmail.com");
        Assertions.assertThat(new UserPrincipal(user)).isEqualTo(userPrincipal);
    }
    @Test
    void 없으면_예외를_발생한다(){
        final UserPrincipalDetailService userPrincipalService = new UserPrincipalDetailService(userRepository);

        assertThatThrownBy(()->userPrincipalService.loadUserByUsername("joyson5582@gmail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("joyson5582@gmail.com");
    }
}

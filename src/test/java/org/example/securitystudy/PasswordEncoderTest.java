package org.example.securitystudy;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Test
    void some(){
        final String encodedValue1 = encoder.encode("123456");

        final boolean isMatched = encoder.matches("123456",encodedValue1);
        assertThat(isMatched).isTrue();
    }
    @Test
    void some1(){
        final String encodeValue1 = encoder.encode("123456");
        final String encodeValue2 = encoder.encode("123456");

        assertThat(encodeValue1).isNotEqualTo(encodeValue2);
    }
}

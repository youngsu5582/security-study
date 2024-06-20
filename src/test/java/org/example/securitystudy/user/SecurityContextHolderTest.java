package org.example.securitystudy.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityContextHolderTest {
    @Test
    void SecurityContext는_Authentication을_가진다() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        final SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        assertThat(securityContext.getAuthentication()).isEqualTo(authentication);
    }
    @Test
    void SecurityContextHolder는_ThreadLocal을_사용해_각_스레드마다_다른_값을_가진다(){
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);

        UserDetailsService userDetailsService = new InMemoryUserDetailsManager(
                User.withUsername("user1").password("password").roles("USER").build(),
                User.withUsername("user2").password("password").roles("USER").build()
        );

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            UserDetails user1 = userDetailsService.loadUserByUsername("user1");
            SecurityContext context1 = SecurityContextHolder.createEmptyContext();
            context1.setAuthentication(new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword(), user1.getAuthorities()));
            SecurityContextHolder.setContext(context1);

            // Simulate some processing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(user1.getUsername());
        };

        Runnable task2 = () -> {
            UserDetails user2 = userDetailsService.loadUserByUsername("user2");
            SecurityContext context2 = SecurityContextHolder.createEmptyContext();
            context2.setAuthentication(new UsernamePasswordAuthenticationToken(user2.getUsername(), user2.getPassword(), user2.getAuthorities()));
            SecurityContextHolder.setContext(context2);

            // Simulate some processing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(user2.getUsername());
        };

        executorService.submit(task1);
        executorService.submit(task2);

        executorService.shutdown();
    }
}

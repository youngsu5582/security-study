package org.example.securitystudy.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    private boolean accountExpired = false;

    public User(final String email, final String password, final List<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
        this.roles = List.of(Role.CUSTOMER);
    }

    public void expired(){
        this.accountExpired = true;
    }
}

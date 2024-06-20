### UserDetails

[관련 링크](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetails.html)

사용자의 인증 정보기능을 제공한다. ( getUsername, getPassword,getAuthorities,isAccountNonExpired... )

기존의 Entity 에 구현을 해도 가능하고, 별도의 클래스를 만들어서 사용도 가능하다.
- 보안의 문제로 `Spring Security`가 직접 구현하지 않는다.

```java
public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(final User user) {
        this.user = user;
    }
}
```

```java
public class UserPrincipal implements UserDetails {
    private final String email;
    private final String password;

    public UserPrincipal(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
```

- 이때 주의해야할 점은 `UserDetails` 가 직렬화 가능해야 하는점이다.

Entity Class 는 `Serializable` 를 상속받지 않으므로 직렬화가 불가능
( 세션 저장등에 사용 가능 )

=> 따라서 하단 방법이 더 괜찮다고 생각

### UserDetailsService

interface

사용자의 데이터를 로드하는 기능(READ)을 제공한다.

`	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;`

- 위 `UserDetails` 인터페이스를 구현한 구현체를 반환한다.
  ( 이때 UserDetails 를 implement 한 custom class 도 당연히 가능 )

#### UserDetailsManager

이 UserDetailsService 를 extends 한 interface

사용자의 데이터 작성하는 기술(WRITE) + 편의성 기능을 제공한다.

- Spring Security가 `InMemoryUserDetailsManager` 와 `InMemoryUserDetailsManager` 를 제공해준다.

( 이들을 사용하지 않으려면, 직접 UserDetailsService or UserDetailsManager 구현후 Bean 등록해야 한다. )

### SecurityContext

Authenction을 가지고 있는 객체
- setter/getter 를 가진다

### SecurityContextHolder

SecurityContext 들을 관리하는 객체
ThreadLocal 을 통해 스레드마다 SecurityContext 를 담당한다.



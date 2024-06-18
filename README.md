## Security

[공식 링크](https://docs.spring.io/spring-security/reference/index.html)

### Password Encoder

[관련 링크](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)

단방향 변환을 수행한다.
( DB 인증,자격 증명등 양방향을 기대할 시 유용하지 않음 )

- 왜 비밀번호를 암호화 해야하는가?
  -> SQL Injection 등을 통해 대량의 덤프로 추출 가능
- 처음에는 단방향 해시로만 저장 ( 해시 통해 추측이 어려울 것이므로 )
  -> 레인보우 테이블 통해 공격
- salted 비밀번호 사용 ( 사용자 비밀번호와 함께 일반 텍스트로 저장)
  -> 이 역시도 작업하기는 쉬워지긴 했음 ( 계산이 어마어마하게 빨라지므로 )
  -> 비밀번호 확인하는데 작업 계수가 오래걸리게 설정하자 ( 비밀번호는 해독하기 어렵게, 시스템에 과도한 부담은 주지않게 )

=> 이런것들을 위해 PasswordEncoder interface 를 제공
( 구현한 bcrypt,scrypt,argon2 등등 )

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

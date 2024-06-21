## Authentication 

인증 관련 요소들을 모아둔 패키지

이해하기 위해 필요한 내용들을 차례대로 설명한다. 

### UsernamePasswordAuthentication


[관련 링크](https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/authentication/UsernamePasswordAuthenticationToken.html)

사용자의 username 과 password 를 가지고 있는 인증 정보

Authentication 을 implements 한다.

- 정적 팩토리 메소드(unauthenticated,authenticated) 를 통해 생성 가능하다.
  ( 왠만하면 unauthenticated 를 통해 생성하고 인증 상태 결정은 외부에 위임하자. )

### AuthenticationManager

Authentication 을 가지고 인증을 진행한다.
- Spring 이 자동으로 감지해서 구성해준다. ( DaoAuthenticationProvider -> PasswordEncoder,UserDetailsService...)

```java
@Override
protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
        UserDetails user) {
    final String presentedPassword = authentication.getCredentials().toString();
    final boolean isPasswordCompromised = this.compromisedPasswordChecker != null
            && this.compromisedPasswordChecker.check(presentedPassword).isCompromised();
    if (isPasswordCompromised) {
        throw new CompromisedPasswordException("The provided password is compromised, please change your password");
    }
    final boolean upgradeEncoding = this.userDetailsPasswordService != null
            && this.passwordEncoder.upgradeEncoding(user.getPassword());
    if (upgradeEncoding) {
        final String newPassword = this.passwordEncoder.encode(presentedPassword);
        user = this.userDetailsPasswordService.updatePassword(user, newPassword);
    }
    return super.createSuccessAuthentication(principal, authentication, user);
}
```
( 이렇게 작동하나, 당연히 여기까지 알필요 X )

### UsernamePasswordAuthenticationFilter

- AbstractAuthenticationProcessingFilter 를 상속받는다

HttpRequest 에서 값을 추출해 EntityManager 를 통해 인증을 진행한다.
( 추출하는 부분 제외, EntityManager 와 동일 )

### AuthenticationEntryPoint

예외시 어떻게 응답을 할지 처리하는 클래스
( ControllerAdvice 와 유사함 )

Filter 는 Spring으로 들어오기전 Tomcat 단 처리 ( ControllerAdvice 를 사용 불가 )
- ObjectMapper 를 통해 값을 변환 or 직접 작성

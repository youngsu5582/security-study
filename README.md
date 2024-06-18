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

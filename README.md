# SPRING ADVANCED

## 주요 기능
- AOP를 통한 로그 기록
   - 사용자 ID, 요청 URL, 요청 시간, 요청 바디를 포함한 로그 기록
- 어드민 사용자만 접근 가능한 API에서의 접근 로그 기록
- 사용자 역할 변경 및 댓글 삭제 기능 구현

###
### *도전과제 1 리펙토링 포인트*

- 이메일 중복 체크: 메서드의 시작 부분에 위치하여, 중복 이메일이 있을 경우 즉시 예외를 던지도록 했습니다.

- 비밀번호 암호화: 이메일 중복 체크 후에만 수행되도록 변경하여 불필요한 작업을 방지했습니다.

- 중복된 이메일이 있을 때 비밀번호 암호화가 발생하지 않으므로 성능이 개선됩니다.


### *도전과제 2 리펙토링 포인트*

- 불필요한 else 제거: 첫 번째 if 조건이 true일 경우 메서드가 예외를 던지므로, 이후의 else 블록이 필요하지 않습니다. 
따라서 바로 다음 조건을 체크합니다.

- 가독성 향상: 이렇게 리팩토링함으로써 코드의 흐름이 더 명확해지고 가독성이 향상됩니다. 각 조건이 독립적으로 처리되므로, 코드의 의도를 더 쉽게 이해할 수 있을 것 같습니다.


### *도전과제 4 테스트 코드*

- 테스트 연습 1 (예상대로 성공하는지에 대한 케이스입니다.)
   encodedPassword를 제대로 비교할 수 있도록 하기 위해
   matches 메서드 호출 시 첫 번째 인자로 rawPassword를, 두 번째 인자로 encodedPassword를 넘기게
   호출 순서를 조정하였습니다.


- 테스트 연습 2(예상대로 예외처리 하는지에 대한 케이스입니다.)



### 1번 케이스

메서드 이름 변경:

- 원래 메서드 이름 : manager_목록_조회_시_Todo가_없다면_NPE_에러를_던진다()

- 변경된 메서드 이름: manager_목록_조회_시_Todo가_없다면_InvalidRequestException_에러를_던진다()


### 2번 케이스

- 할일 조회 설정:
given(todoRepository.findById(todoId)).willReturn(Optional.empty());를 사용하여 주어진 todoId로 할 일을 찾지 못하는 상황을 시뮬레이션합니다.

- 예외 확인:
assertThrows(InvalidRequestException.class, () -> { ... });를 사용하여 InvalidRequestException이 발생하는지 확인합니다.

- 에러 메시지 검증:
assertEquals("Todo not found", exception.getMessage());를 통해 발생한 예외의 메시지가 예상과 일치하는지 검증합니다.


### 3번 케이스

- todo.getUser() == null일 때 InvalidRequestException을 던지는 조건을 추가했습니다.
이 조건을 통해 Todo의 작성자가 존재하지 않을 경우 적절한 예외가 발생하게 될 것이고 ‘todo의_user가_null인_경우_예외가_발생한다()’ 테스트 성공에 요건에 부합할 것이라 봅니다.


#
## AOP

- common 패키지의 하위 패키지인 annotation 패키지에 Logging 어노테이션 클래스 생성
- common 패키지 안에 aspect 하위 패키지 생성 및 AOP설정 클래스 LoggingAspect 생성

### 1. 의존성 추가
- Spring AOP를 사용하기 위한 의존성을 프로젝트에 추가했습니다.

### 2. 로그 기록용 어노테이션 생성
- @Logging 어노테이션을 정의하여 로그 기록이 필요한 메서드에 적용할 수 있도록 했습니다.
### 3. AOP 설정 클래스 생성
- LoggingAspect 클래스를 생성하여 AOP를 설정했습니다.
- @Aspect와 @Component 어노테이션을 사용하여 AOP 기능을 활성화했습니다.
- @Around 어노테이션으로 로그 기록을 위한 메서드를 정의했습니다.

### 4. 로그 기록 메서드 구현
- 요청 시각, 요청 URL, 사용자 ID를 기록하는 로직을 logExecutionTime 메서드에 추가했습니다.
- 요청 URL은 HttpServletRequest를 통해 가져오고, 사용자 ID는 SecurityContextHolder를 통해 가져오는 로직을 구현했습니다.

### 5. 컨트롤러 메서드에 AOP 적용
- CommentAdminController의 deleteComment 메서드와 UserAdminController의 changeUserRole 메서드에 @Logging 어노테이션을 추가하여 AOP가 적용되도록 했습니다.

### 6. 로그 출력 형식 설정
- SLF4J 로거를 사용하여 로그 메시지를 포맷했습니다. 로그에는 사용자 ID, 요청 URL, 실행 시간을 포함했습니다.

### 7. 사용자 ID 가져오기
- 현재 인증된 사용자의 ID를 가져오는 메서드를 구현했습니다. 이 메서드는 Spring Security의 SecurityContextHolder를 사용하여 현재 사용자 정보를 확인합니다.
  
- Authentication 객체를 통해 현재 사용자 인증 상태를 확인합니다.
- getPrincipal() 메서드를 사용하여 사용자 정보를 가져오고, 이를 User 객체로 캐스팅합니다.


### 8. 테스트 및 검증
- AOP가 적용된 메서드에 대해 실제 요청을 보내어 로그가 정상적으로 기록되는지 확인할 수 있도록 했습니다.




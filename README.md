### USER + AUTH API 명세서

| API        | 엔드포인트                                     | HTTP 메서드 | 요청 데이터                                          | 응답 데이터                                | 상태코드                |
|------------|-------------------------------------------|----------|-------------------------------------------------|--------------------------------------------|---------------------|
| 회원가입       | `/api/users/signup`                       | POST     | email, password, nickname, bio, profileImageUrl | 없음                                       | 200 OK, 400, 409    |
| 로그인        | `/auth/login`                             | POST     | userEmail, userPassword                         | userId, userEmail, userName, isActive      | 200 OK, 401, 404    |
| 로그아웃       | `/auth/logout`                            | POST     | 없음 (세션 기반)                                      | "로그아웃 되었습니다."                      | 200 OK, 401         |
| 로그인 상태확인   | `/auth/check`                             | GET      | 없음 (세션 기반)                                      | 로그인된 사용자 정보                         | 200 OK, 401         |
| 프로필 조회     | `/api/users/{targetUserId}`               | GET      | PathVariable: targetUserId                      | email, nickname, bio, profileImageUrl      | 200 OK, 404         |
| 프로필 수정     | `/api/users/profile`                      | PUT      | nickname, bio, profileImageUrl                  | 없음                                       | 200 OK, 401, 404    |
| 비밀번호 변경    | `/api/users/password`                     | PATCH    | currentPassword, newPassword                    | 없음                                       | 200 OK, 400         |
| 회원 탈퇴      | `/api/users`                              | DELETE   | password                                        | 없음                                       | 204 No Content, 400 |


### USER + AUTH API 작동 방식 설명

- **회원가입**  
  사용자가 입력한 이메일, 비밀번호, 닉네임 등 정보를 검증한 후, 비밀번호는 암호화하여 DB에 저장한다.

- **로그인**  
  사용자가 입력한 이메일과 비밀번호를 DB와 비교하여 일치할 경우, 세션을 생성하고 사용자 정보를 세션에 저장한다.

- **로그아웃**  
  현재 세션을 제거하여 로그아웃 상태로 만든다.

- **로그인 상태 확인**  
  세션에 로그인 정보가 존재하는지 확인하고, 존재할 경우 사용자 정보를 반환한다.

- **프로필 조회**  
  PathVariable로 전달된 사용자 ID를 기준으로 해당 사용자의 프로필 정보를 조회한다.

- **프로필 수정**  
  현재 로그인된 사용자의 세션 ID를 기반으로 닉네임, 자기소개, 프로필 이미지를 수정한다.

- **비밀번호 변경**  
  세션에 저장된 로그인 사용자 ID로 사용자를 조회하고, 기존 비밀번호 확인 후 새 비밀번호로 변경한다.

- **회원 탈퇴**  
  세션을 통해 로그인된 사용자의 ID를 가져와, 비밀번호 검증 후 deleted 상태값을 true로 변경 (soft delete)

### User 와 Auth API 통합 이유

사용자와 인증 기능은 서로 밀접하게 연결되어 있음.  
예를 들어 프로필 조회나 수정, 탈퇴 기능은 로그인된 사용자 정보가 필요하므로 인증을 거친 사용자만 접근할 수 있도록 설계됨.  
따라서 사용자와 인증 도메인은 기능상 분리되어 있어도 실제 흐름상 함께 사용되기 때문에 명세서상 통합하여 관리.






### 예외 코드 목록

| 예외 코드                                  | 설명                                             |
|----------------------------------------|------------------------------------------------|
| `INVALID_EMAIL_FORMAT`                 | 이메일 형식이 올바르지 않음                                |
| `INVALID_PASSWORD_FORMAT`              | 비밀번호 형식이 요구 조건(대문자+영문+숫자+특수문자 포함 8자 이상)에 맞지 않음 |
| `DUPLICATE_EMAIL`                      | 이미 존재하는 이메일 주소                                 |
| `USER_NOT_FOUND`                       | 사용자를 찾을 수 없음                                   |
| `INVALID_PASSWORD`                     | 비밀번호가 일치하지 않음                                  |
| `SAME_AS_OLD_PASSWORD`                 | 새 비밀번호가 현재 비밀번호와 동일함                           |
| `UNAUTHORIZED`                         | 로그인되지 않은 상태에서 접근 시도                            |
| `UNAUTHORIZED_USER`                    | 게시글, 댓글 등의 리소스를 작성자가 아닌 사용자가 수정 또는 삭제 시도       |
| `LOGIN_REQUIRED`                       | 로그인하지 않은 상태에서 인증이 필요한 API에 접근                  |
| `DEACTIVATED_USER`                     | 탈퇴 처리된 사용자가 로그인 시도                             |
| `WRONG_PARENT_TYPE`                    | 잘못된 타입 값                                       |
| `DOES_NOT_EXIST`                       | 제공된 파라미터에 해당하는 대상이 없음                          |
| `ACCESS_DENIED`                        | 댓글과 답글은 그 작성자나 피드의 작성자만 수정 및 삭제 가능             |
| `PREEMPTIVE_ACTION_REQUIRED`           | 댓글과 답글의 삭제는 먼저 비활성화 시킨 후에 가능                   |
| `UNATUHORIZED_DATA_MANUPILATION_FOUND` | 정상적인 방법으로 입력될 수 없는 데이터가 DB에 입력된것을 확인           |

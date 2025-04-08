package com.example.newsfeedproject.auth;


import com.example.newsfeedproject.auth.Dto.LoginRequestDto;
import com.example.newsfeedproject.auth.Dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request
    ) {
        String userId = requestDto.getUserId();
        String userPw = requestDto.getUserPw();
        HttpSession session = request.getSession(false);

        if (SessionManager.isLogin(session)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 로그인된 사용자입니다.");
        }

        // 로그인 검증 로직
        // id와 pw가 일치하는지+ 유효한지 체크
        //아이디와 비밀번호가 맞는지 DB나 저장소에서 확인해주는 역할을 합니다.+ LoginResponseDto 리턴
        LoginResponseDto userInfo = userService.authenticate(requestDto);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디 또는 비밀번호가 틀렸습니다.");
        }
        // 세션 없다면 생성
        session = request.getSession();
        // 서버에 세션 저장
        SessionManager.setLoginUser(session, userInfo);

  //return new ResponseEntity<>(userInfo, HttpStatus.OK);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        // 로그인 안 한 상태 + 세션 없는 상태라면
        if (session == null || !SessionManager.isLogin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 상태가 아닙니다.");
        }

        SessionManager.logout(session);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || !SessionManager.isLogin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인되어 있지 않습니다.");
        }
        LoginResponseDto user = SessionManager.getLoginUser(session);
        return ResponseEntity.ok(user);
    }

}

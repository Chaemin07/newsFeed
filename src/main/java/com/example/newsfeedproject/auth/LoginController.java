//package com.example.newsfeedproject.auth;
//
//
//import com.example.newsfeedproject.auth.Dto.LoginRequestDto;
//import com.example.newsfeedproject.auth.Dto.LoginResponseDto;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import org.hibernate.Session;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.example.newsfeedproject.user.service.UserService;
//
//
//
///**
// * 로그인/로그아웃 및 인증 관련 요청을 처리하는 컨트롤러입니다.
// * <p>
// * 로그인 상태 확인, 로그인 처리, 로그아웃 처리 등의 기능을 제공합니다.
// * </p>
// */
//@RequestMapping("/auth")
//@RestController
//@AllArgsConstructor
//public class LoginController {
//
//    private final UserService userService;
//
//
//    /**
//     * 로그인 요청을 처리합니다.
//     *
//     * <p>요청으로 들어온 아이디와 비밀번호를 검증하고,
//     * 세션이 없다면 생성 후 사용자 정보를 저장합니다.</p>
//     *
//     * @param requestDto 로그인 요청 정보 (아이디, 비밀번호)
//     * @param request    현재 HTTP 요청 객체
//     * @return 로그인 성공 시 사용자 정보, 실패 시 상태 메시지 반환
//     */
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestBody LoginRequestDto requestDto,
//            HttpServletRequest request
//    ) {
//        String userId = requestDto.getUserId();
//        String userPw = requestDto.getUserPw();
//        HttpSession session = request.getSession(false);
//
//        if (SessionManager.isLogin(session)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("이미 로그인된 사용자입니다.");
//        }
//
//        // 로그인 검증 로직
//        // id와 pw가 일치하는지+ 유효한지 체크
//        //아이디와 비밀번호가 맞는지 DB나 저장소에서 확인해주는 역할을 합니다.+ LoginResponseDto 리턴
//        LoginResponseDto userInfo = userService.authenticate(requestDto);
//        if (userInfo == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("아이디 또는 비밀번호가 틀렸습니다.");
//        }
//        // 세션 없다면 생성
//        session = request.getSession();
//        // 서버에 세션 저장
//        SessionManager.setLoginUser(session, userInfo);
//
//  //return new ResponseEntity<>(userInfo, HttpStatus.OK);
//        return ResponseEntity.ok(userInfo);
//    }
//
//
//    /**
//     * 로그아웃 요청을 처리합니다.
//     *
//     * <p>세션이 존재하고 로그인된 상태인 경우 세션을 무효화하여 로그아웃 처리합니다.</p>
//     *
//     * @param request 현재 HTTP 요청 객체
//     * @return 로그아웃 결과 메시지
//     */
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        // 로그인 안 한 상태 + 세션 없는 상태라면
//        if (!SessionManager.isLogin(session)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("로그인 상태가 아닙니다.");
//        }
//
//        SessionManager.logout(session);
//        return ResponseEntity.ok("로그아웃 되었습니다.");
//    }
//
//    /**
//     * 로그인 상태 확인 API입니다.
//     *
//     * <p>세션이 존재하고 로그인된 사용자 정보가 있을 경우 로그인 상태로 간주합니다.</p>
//     *
//     * @param request 현재 HTTP 요청 객체
//     * @return 로그인된 사용자 정보 또는 로그인되지 않았다는 메시지
//     */
//    @GetMapping("/check")
//    public ResponseEntity<?> checkLogin(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        // 예외 발생시 -> 전역 예외처리 핸들러에서 처리
//        SessionManager.validateLogin(request);
//
//        LoginResponseDto user = SessionManager.getLoginUser(session);
//        return ResponseEntity.ok(user);
//    }
//
//}
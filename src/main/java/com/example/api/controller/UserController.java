package com.example.api.controller;

import com.example.api.DTO.UserDto;
import com.example.api.domain.LoginRequest;
import com.example.api.domain.User;
import com.example.api.service.UserService;
import com.example.api.service.CurrUserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;
    private final CurrUserSessionService currUserSessionService;

    @Autowired
    public UserController(UserService userService, CurrUserSessionService currUserSessionService) {
        this.userService = userService;
        this.currUserSessionService = currUserSessionService;
    }
    //login and create session
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        try {
            User user = userService.login(loginRequest);
            System.out.println("login success");

            if (user != null) {
                // 로그인 성공
                HttpSession session = httpServletRequest.getSession(true);
                System.out.println("success in getting session");
                session.setAttribute("userId", user.getId());

                System.out.println(session.getAttribute("userId"));
                session.setMaxInactiveInterval(1800);

                // Save session information to Redis
                currUserSessionService.saveSession(session.getId(), user.getId());  // 변경된 부분

                UserDto userDto = convertToDto(user);
                System.out.println(session.getAttribute("userId"));
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            } else {
                // 로그인 실패
                System.out.println("login failed 1");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (RuntimeException e) {
            System.out.println("login failed 2");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // logout and delete session
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession(false);
            if (session != null) {
                session.invalidate(); // 세션 무효화
            }
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during logout", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // check session information(check current user)
    @GetMapping("/check-session")
    public ResponseEntity<UserDto> checkSession(HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession(false);

            // If there's no session, return null
            if (session == null || session.getAttribute("userId") == null) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }

            // get userId from current session
            Long userId = (Long) session.getAttribute("userId");

            // find user with userId
            Optional<User> userOptional = userService.findOneUser(userId);

            // If user exists, return user dto
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDto userDto = convertToDto(user);
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            } else {
                // If there's no user, return UNAUTHORIZED
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // sign up
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody User user) {
        try {
            // 중복 체크
            if (userService.isUsernameExists(user.getName())) {
                // 이미 존재하는 유저일 경우
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Long userId = userService.join(user);
            Optional<User> createdUser = userService.findOneUser(userId);

            if (createdUser.isPresent()) {
                UserDto userDto = convertToDto(createdUser.get());
                System.out.println("User already exists: " + user.getName());
                return new ResponseEntity<>(userDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // Convert User to UserDto
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setPw(user.getPw());
        return userDto;
    }



}
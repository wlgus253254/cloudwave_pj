package com.example.api.service;

import com.example.api.domain.LoginRequest;
import com.example.api.domain.User;
import com.example.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Login
    public User login(LoginRequest req){
        Optional<User> optionalUser = userRepository.findByName(req.getName());
        if(optionalUser.isEmpty()){
            System.out.println("cannot find user");
            return null;
        }
        User user = optionalUser.get();

        // 사용자가 입력한 비밀번호와 데이터베이스에서 조회한 비밀번호 출력
        System.out.println("사용자 id: " + user.getId());
        System.out.println("사용자 name: " + user.getName());
        System.out.println("사용자가 입력한 비밀번호: [" + req.getPw() + "]");
        System.out.println("데이터베이스에서 조회한 비밀번호: [" + user.getPw() + "]");
//        if(!user.getPw().equals(req.getPw())){
//            System.out.println("cannot match pw");
//            return null;
//        }

        if (user.getPw().compareTo(req.getPw()) != 0) {
            System.out.println("cannot match pw");
            return user;
        }

        return user;
    }

    // sign up
    public Long join(User user){
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }
    public ResponseEntity<String> validateDuplicateUser(User user) {
        return userRepository.findByName(user.getName())
                .map(m -> ResponseEntity.badRequest().body("이미 가입한 이메일입니다."))
                .orElse(null);
    }

    // find all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    // find one user by id
    public Optional<User> findOneUser(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean isUsernameExists(String username) {
        Optional<User> existingUser = userRepository.findByName(username);
        return existingUser.isPresent();
    }
}
package com.first.demo.controller;

import com.first.demo.dao.User;
import com.first.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController // 메서드의 반환 값이 JSON,Response Body(@Controller는 View 반환)
@RequestMapping("/api/users") // 해당 클래스에서는 회원 정보 조회, 수정, 삭제 같은 사용자 관리만 구현함 
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found : 리소스(사용자,데이터)가 존재하지 않을 때 
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content : 클라이언트가 요청을 성공적으로 처리했지만, 별도로 반환할 데이터가 없을 때 
    }
}

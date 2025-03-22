package com.first.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first.demo.dao.CustomUserDetails;
import com.first.demo.dao.User;
import com.first.demo.service.UserService;

@RestController // 메서드의 반환 값이 JSON,Response Body(@Controller는 View 반환)
@RequestMapping("/api/users") // 해당 클래스에서는 회원 정보 조회, 수정, 삭제 같은 사용자 관리만 구현함 
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // 모든 사용자 목록 조회(/api/users/)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUserIds();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build(); //  204 No Content : 클라이언트가 요청을 성공적으로 처리했지만, 별도로 반환할 데이터가 없을 때 
        }
        return ResponseEntity.ok(users); // 200 OK
    }

    // 특정 사용자 조회(/api/users/me)
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(Principal principal) {
        String email = principal.getName(); 
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    // 사용자 정보 업데이트(/api/users/me) : 일부 필드만 업데이트하므로 PATCH 사용 
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> updates,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){          
        try{
            User user = userDetails.getUser();
            User savedUser = userService.updateUser(user, updates);
            return ResponseEntity.ok(savedUser); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    // 사용자 삭제(/api/users/me))
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content 
    }
}

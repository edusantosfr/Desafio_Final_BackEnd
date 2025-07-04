package com.teach.gram.controller;

import com.teach.gram.dto.req.user.UserPatchReqDTO;
import com.teach.gram.dto.res.post.PostResDTO;
import com.teach.gram.service.UserService;
import com.teach.gram.dto.req.login.LoginReqDTO;
import com.teach.gram.dto.req.user.UserReqDTO;
import com.teach.gram.dto.res.login.LoginResDTO;
import com.teach.gram.dto.res.user.UserResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResDTO createUser(
            @RequestBody UserReqDTO dto
            ) {
        UserResDTO response = userService.createUser(dto);

        return response;
    }

    @PostMapping("/login")
    public LoginResDTO login(
            @RequestBody LoginReqDTO dto
    ) {
        LoginResDTO response = userService.login(dto);

        return response;
    }

    @GetMapping
    public ResponseEntity<List<UserResDTO>> getAllUsers() {

        List<UserResDTO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public UserResDTO updateProfile(
            @PathVariable("id") Long id,
            @RequestBody UserPatchReqDTO userPatchReqDTO
    ) {
        UserResDTO response = userService.updateProfile(id, userPatchReqDTO);

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity deleteProfile(
            @PathVariable("id") Long id
            ) {
        userService.deleteProfile(id);

        return ResponseEntity.noContent().build();
    }
}

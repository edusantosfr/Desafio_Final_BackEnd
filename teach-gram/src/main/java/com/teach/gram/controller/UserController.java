package com.teach.gram.controller;

import com.teach.gram.dto.req.user.UserPatchReqDTO;
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

    // Registrar novo usuário
    @PostMapping("/register")
    public UserResDTO createUser(
            @RequestBody UserReqDTO dto
    ) {
        return userService.createUser(dto);
    }

    // Login de usuário
    @PostMapping("/login")
    public LoginResDTO login(
            @RequestBody LoginReqDTO dto
    ) {
        return userService.login(dto);
    }

    // Buscar todos os usuários
    @GetMapping
    public ResponseEntity<List<UserResDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Atualizar perfil de um usuário
    @PatchMapping("/{id}/update")
    public UserResDTO updateProfile(
            @PathVariable("id") Long id,
            @RequestBody UserPatchReqDTO userPatchReqDTO
    ) {
        return userService.updateProfile(id, userPatchReqDTO);
    }

    // Deletar perfil de um usuário (soft delete)
    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable("id") Long id
    ) {
        userService.deleteProfile(id);

        return ResponseEntity.noContent().build();
    }
}

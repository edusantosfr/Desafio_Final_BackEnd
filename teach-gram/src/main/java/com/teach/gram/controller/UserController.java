package com.teach.gram.controller;

import com.teach.gram.dto.req.user.UserInfoPatchReqDTO;
import com.teach.gram.dto.req.user.UserPatchReqDTO;
import com.teach.gram.model.User;
import com.teach.gram.service.UserService;
import com.teach.gram.dto.req.login.LoginReqDTO;
import com.teach.gram.dto.req.user.UserReqDTO;
import com.teach.gram.dto.res.login.LoginResDTO;
import com.teach.gram.dto.res.user.UserResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Registrar novo usuario
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

    // Buscar todos os usuários não deletados
    @GetMapping
    public ResponseEntity<List<UserResDTO>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Pegar perfil do usuário logado
    @GetMapping("/loged")
    public UserResDTO getLogedUser() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.getLogedUser(user.getId());
    }

    //Pegar o perfil de um usuário pelo id
    @GetMapping("/user/{id}")
    public UserResDTO getUserById(
            @PathVariable Long id
    ) {;
        return userService.getUserById(id);
    }

    // Atualizar as informações públicas do perfil de um usuário
    @PatchMapping("/update")
    public UserResDTO updateProfile(
            @RequestBody UserPatchReqDTO userPatchReqDTO
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.updateProfile(user.getId(), userPatchReqDTO);
    }

    // Atualizar as informações privadas do perfil de um usuário
    @PatchMapping("/update/info")
    public LoginResDTO updateProfileInfo(
            @RequestBody UserInfoPatchReqDTO userInfoPatchReqDTO
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.updateProfileInfo(user.getId(), userInfoPatchReqDTO);
    }

    // Adicionar um usuário como amigo
    @PostMapping("/friends/{friendId}/add")
    public ResponseEntity<Void> addFriend(
            @PathVariable Long friendId
    ) {
        userService.addFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    // Remover um usuário de amigo
    @DeleteMapping("/friends/{friendId}/remove")
    public ResponseEntity<Void> removeFriend(
            @PathVariable Long friendId
    ) {
        userService.removeFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    // Pegar todos os amigos do usuário pelo id
    @GetMapping("/friends/{userId}")
    public ResponseEntity<Set<UserResDTO>> getUserListFriends(
            @PathVariable Long userId
    ) {
        Set<UserResDTO> friends = userService.getUserListFriends(userId);
        return ResponseEntity.ok(friends);
    }

    // Pegar todos os amigos do usuário logado
    @GetMapping("/friends")
    public ResponseEntity<Set<UserResDTO>> getMyListFriends() {
        Set<UserResDTO> friends = userService.getMyListFriends();
        return ResponseEntity.ok(friends);
    }

    // Deletar perfil do usuário
    @PatchMapping("/delete")
    public ResponseEntity<Void> deleteProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteProfile(user.getId());

        return ResponseEntity.noContent().build();
    }
}

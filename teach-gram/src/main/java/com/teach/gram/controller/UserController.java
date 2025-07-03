package com.teach.gram.controller;

import com.teach.gram.service.UserService;
import com.teach.gram.dto.req.login.LoginReqDTO;
import com.teach.gram.dto.req.user.UserReqDTO;
import com.teach.gram.dto.res.LoginResDTO;
import com.teach.gram.dto.res.UserResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

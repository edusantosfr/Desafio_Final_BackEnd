package com.teach.gram.service;

import com.teach.gram.dto.req.login.LoginReqDTO;
import com.teach.gram.dto.req.user.UserReqDTO;
import com.teach.gram.dto.res.LoginResDTO;
import com.teach.gram.dto.res.UserResDTO;
import com.teach.gram.model.User;
import com.teach.gram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserResDTO createUser(UserReqDTO dto) {
        if (dto.username() == null || dto.username().isEmpty())
            throw new RuntimeException("Username cannot be null or empty");

        if (dto.password() == null || dto.password().isEmpty())
            throw new RuntimeException("Password cannot be null or empty");

        if (userRepository.existsByUsername(dto.username()))
            throw new RuntimeException("Username already in use");

        User user = new User();
        user.setName(dto.name());
        user.setMail(dto.email());
        user.setUsername(dto.username());
        user.setDescription(dto.description());
        user.setPhone(dto.phone());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setProfileLink(dto.profileLink());

        userRepository.save(user);

        return new UserResDTO(user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public LoginResDTO login(LoginReqDTO dto) {
        if (dto.username() == null || dto.username().isEmpty())
            throw new RuntimeException("Username cannot be null or empty");

        if (dto.password() == null || dto.password().isEmpty())
            throw new RuntimeException("Password cannot be null or empty");

        Optional<User> optionalUser = userRepository.findByUsername(dto.username());

        if (optionalUser.isEmpty())
            throw new RuntimeException("User not found");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                dto.username(), dto.password());

        authenticationManager.authenticate(token);

        return tokenService.generateToken(optionalUser.get());
    }
}
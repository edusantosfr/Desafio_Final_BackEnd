package com.teach.gram.service;

import com.teach.gram.dto.req.login.LoginReqDTO;
import com.teach.gram.dto.req.user.UserInfoPatchReqDTO;
import com.teach.gram.dto.req.user.UserPatchReqDTO;
import com.teach.gram.dto.req.user.UserReqDTO;
import com.teach.gram.dto.res.login.LoginResDTO;
import com.teach.gram.dto.res.user.UserResDTO;
import com.teach.gram.model.User;
import com.teach.gram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

        if (userRepository.existsByUsername(dto.username()))
            throw new RuntimeException("Username already in use");

        if (userRepository.existsByMail(dto.mail()))
            throw new RuntimeException("Email already in use");

        if (userRepository.existsByPhone(dto.phone()))
            throw new RuntimeException("Phone already in use");

        User user = new User();
        user.setName(dto.name());
        user.setMail(dto.mail());
        user.setUsername(dto.username());
        user.setDescription(dto.description());
        user.setPhone(dto.phone());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setProfileLink(dto.profileLink());
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public LoginResDTO login(LoginReqDTO dto) {

        if (dto.mail() == null || dto.mail().isEmpty())
            throw new RuntimeException("E-mail cannot be null or empty");

        if (dto.password() == null || dto.password().isEmpty())
            throw new RuntimeException("Password cannot be null or empty");

        User user = userRepository.findByMail(dto.mail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.getDeleted())
            throw new RuntimeException("User not found");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                dto.password()
        );

        authenticationManager.authenticate(authToken);

        return tokenService.generateToken(user);
    }

    public List<UserResDTO> getAllUsers() {
        List<User> users = userRepository.findByDeletedFalse();

        return users.stream()
                .map(user -> new UserResDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getMail(),
                        user.getPhone(),
                        user.getDescription(),
                        user.getProfileLink()
                ))
                .toList();
    }

    public UserResDTO getLogedUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public UserResDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public UserResDTO updateProfile(Long id, UserPatchReqDTO userPatchReqDTO) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAuth.getId().equals(id)) {
            throw new RuntimeException("You are not allowed to update this profile.");
        }

        User user = userRepository.findById(userAuth.getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setProfileLink(userPatchReqDTO.profileLink());
        user.setName(userPatchReqDTO.name());
        user.setUsername(userPatchReqDTO.username());
        user.setDescription(userPatchReqDTO.description());
        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public LoginResDTO updateProfileInfo(Long id, UserInfoPatchReqDTO userInfoPatchReqDTO) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAuth.getId().equals(id)) {
            throw new RuntimeException("You are not allowed to update this profile.");
        }

        User user = userRepository.findById(userAuth.getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setName(userInfoPatchReqDTO.name());
        user.setMail(userInfoPatchReqDTO.mail());
        user.setPhone(userInfoPatchReqDTO.phone());

        if (userInfoPatchReqDTO.password() != null && !userInfoPatchReqDTO.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userInfoPatchReqDTO.password()));
        }

        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);

        return tokenService.generateToken(user);
    }

    public void deleteProfile(Long id) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAuth.getId().equals(id)) {
            throw new RuntimeException("You are not allowed to delete this profile.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setDeleted(true);
        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);
    }
}
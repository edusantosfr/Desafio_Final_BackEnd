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

    public UserResDTO createUser(UserReqDTO userReqDTO) {

        if (userRepository.existsByUsername(userReqDTO.username()))
            throw new RuntimeException("Username em uso");

        if (userRepository.existsByMail(userReqDTO.mail()))
            throw new RuntimeException("Email em uso");

        if (userRepository.existsByPhone(userReqDTO.phone()))
            throw new RuntimeException("Celular em uso");

        if (userReqDTO.mail() == null || userReqDTO.mail().isEmpty())
            throw new RuntimeException("E-mail não pode ser nulo");

        if (userReqDTO.password() == null || userReqDTO.password().isEmpty())
            throw new RuntimeException("Senha não pode ser nula");

        if (userReqDTO.phone() == null || userReqDTO.phone().isEmpty())
            throw new RuntimeException("Celular não pode ser nulo");

        User user = new User();
        user.setName(userReqDTO.name());
        user.setMail(userReqDTO.mail());
        user.setUsername(userReqDTO.username());
        user.setDescription(userReqDTO.description());
        user.setPhone(userReqDTO.phone());
        user.setPassword(passwordEncoder.encode(userReqDTO.password()));
        user.setProfileLink(userReqDTO.profileLink());
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public LoginResDTO login(LoginReqDTO loginReqDTO) {

        if (loginReqDTO.mail() == null || loginReqDTO.mail().isEmpty())
            throw new RuntimeException("E-mail não pode ser nulo");

        if (loginReqDTO.password() == null || loginReqDTO.password().isEmpty())
            throw new RuntimeException("Senha não pode ser nula");

        User user = userRepository.findByMail(loginReqDTO.mail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getDeleted())
            throw new RuntimeException("Usuário não encontrado");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                loginReqDTO.password()
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public UserResDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserResDTO(user.getId(), user.getName(), user.getMail(), user.getUsername(), user.getDescription(), user.getPhone(), user.getProfileLink());
    }

    public UserResDTO updateProfile(Long id, UserPatchReqDTO userPatchReqDTO) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAuth.getId().equals(id)) {
            throw new RuntimeException("Você não pode atualizar esse perfil");
        }

        User user = userRepository.findById(userAuth.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
            throw new RuntimeException("Você não pode atualizar esse perfil");
        }

        User user = userRepository.findById(userAuth.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
            throw new RuntimeException("Você não pode deletar esse perfil");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setDeleted(true);
        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);
    }
}
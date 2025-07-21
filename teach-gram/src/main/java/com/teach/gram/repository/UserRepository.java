package com.teach.gram.repository;

import com.teach.gram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByMail(String mail);

    boolean existsByPhone(String phone);

    Optional<User> findByMail(String mail);

    Optional<User> findByUsername(String username);

    List<User> findByDeletedFalse();

    Optional<User> findByIdAndDeletedFalse(Long id);
}

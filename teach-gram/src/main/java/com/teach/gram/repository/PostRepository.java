package com.teach.gram.repository;

import com.teach.gram.model.Post;
import com.teach.gram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByTituloAndUsuario(String titulo, User usuario);

    List<Post> findByUsuario(User usuario);

    Optional<Post> findByIdAndUsuario(Long id, User usuario);
}

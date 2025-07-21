package com.teach.gram.repository;

import com.teach.gram.model.Post;
import com.teach.gram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUser(Long id, User usuario);

    List<Post> findByUserIdAndPrivatePostFalseAndDeletedFalse(Long userId);

    List<Post> findByPrivatePostFalseAndDeletedFalse();

    List<Post> findByUserAndDeletedFalse(User user);
}

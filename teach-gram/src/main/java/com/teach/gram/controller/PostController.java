package com.teach.gram.controller;

import com.teach.gram.dto.req.post.PostPatchReqDTO;
import com.teach.gram.dto.req.post.PostReqDTO;
import com.teach.gram.dto.res.post.PostResDTO;
import com.teach.gram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Criar um novo post
    @PostMapping
    public ResponseEntity<PostResDTO> createPost(
            @RequestBody PostReqDTO dto
    ) {
        return ResponseEntity.ok(postService.createPost(dto));
    }

    // Buscar todos os posts do usuário autenticado
    @GetMapping
    public ResponseEntity<List<PostResDTO>> getAllMyPosts() {
        return ResponseEntity.ok(postService.getAllMyPosts());
    }

    // Buscar posts públicos e ativos de um usuário específico
    @GetMapping("/users/{userId}/public")
    public ResponseEntity<List<PostResDTO>> getPublicAndActivePosts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(postService.getPublicAndActivePostsByUserId(userId));
    }

    // Buscar meu post pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PostResDTO> getMyPostById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.getMyPostById(id));
    }

    // Buscar qualquer post pelo ID
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResDTO> getPostById(
            @PathVariable Long id
    ) {
        PostResDTO post = postService.getPostById(id);

        return ResponseEntity.ok(post);
    }

    // Atualizar um post (título, descrição, etc.)
    @PatchMapping("/{id}/update")
    public ResponseEntity<PostResDTO> updatePost(
            @PathVariable Long id,
            @RequestBody PostPatchReqDTO postPatchReqDTO
    ) {
        return ResponseEntity.ok(postService.updatePost(id, postPatchReqDTO));
    }

    // Tornar post privado
    @PatchMapping("/{id}/private")
    public ResponseEntity<Void> updatePostPrivateTrue(
            @PathVariable Long id
    ) {
        postService.updatePostPrivateTrue(id);

        return ResponseEntity.noContent().build();
    }

    // Tornar post público
    @PatchMapping("/{id}/public")
    public ResponseEntity<Void> updatePostPrivateFalse(
            @PathVariable Long id
    ) {
        postService.updatePostPrivateFalse(id);

        return ResponseEntity.noContent().build();
    }

    // Deletar um post (soft delete, provavelmente)
    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id
    ) {
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }
}

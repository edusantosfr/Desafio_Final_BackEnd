package com.teach.gram.controller;

import com.teach.gram.dto.req.post.PostPatchReqDTO;
import com.teach.gram.dto.req.post.PostReqDTO;
import com.teach.gram.dto.res.post.PostTimeAndUserResDTO;
import com.teach.gram.dto.res.post.PostTimeResDTO;
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
    @PostMapping("/create")
    public ResponseEntity<PostTimeResDTO> createPost(
            @RequestBody PostReqDTO postReqDTO
    ) {
        return ResponseEntity.ok(postService.createPost(postReqDTO));
    }

    // Buscar todos os posts do usuário logado
    @GetMapping("/allMyPosts")
    public ResponseEntity<List<PostTimeResDTO>> getAllMyPosts() {
        return ResponseEntity.ok(postService.getAllMyPosts());
    }

    // Buscar posts públicos e ativos de um usuário específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostTimeResDTO>> getPublicAndActivePosts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(postService.getPublicAndActivePostsByUserId(userId));
    }

    // Buscar posts públicos e ativos de um usuário específico
    @GetMapping("/users/public")
    public ResponseEntity<List<PostTimeAndUserResDTO>> getRecentPublicAndActivePosts(
    ) {
        return ResponseEntity.ok(postService.getRecentPublicAndActivePosts());
    }

    // Buscar meu post pelo ID X
    @GetMapping("/{id}")
    public ResponseEntity<PostTimeResDTO> getMyPostById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.getMyPostById(id));
    }

    // Buscar qualquer post pelo ID X
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostTimeResDTO> getPostById(
            @PathVariable Long id
    ) {
        PostTimeResDTO post = postService.getPostById(id);

        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{id}/update/likes")
    public ResponseEntity<Void> updatePostLikes(@PathVariable Long id) {
        postService.updatePostLikes(id);
        return ResponseEntity.ok().build();
    }

    // Atualizar um post (título, descrição, etc.)
    @PatchMapping("/{id}/update")
    public ResponseEntity<PostTimeResDTO> updatePost(
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

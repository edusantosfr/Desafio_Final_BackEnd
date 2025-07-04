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

    @PostMapping
    public PostResDTO createPost(
            @RequestBody PostReqDTO dto
    ) {
        PostResDTO response = postService.createPost(dto);

        return response;
    }

    @GetMapping
    public ResponseEntity<List<PostResDTO>> getAllMyPosts() {

        List<PostResDTO> posts = postService.getAllMyPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/users/{userId}/posts/public")
    public ResponseEntity<List<PostResDTO>> getPublicAndActivePosts(
            @PathVariable Long userId
    ) {
        List<PostResDTO> posts = postService.getPublicAndActivePostsByUserId(userId);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResDTO> getTaskById(
            @PathVariable Long id
    ) {
        PostResDTO task = postService.getTaskById(id);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResDTO> updateTitleOrStatus(
            @PathVariable("id") Long id,
            @RequestBody PostPatchReqDTO postPatchReqDTO
    ) {
        PostResDTO task = postService.updateTitleOrStatus(id, postPatchReqDTO);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Long id
            ) {
        postService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

package com.teach.gram.service;

import com.teach.gram.dto.req.post.PostPatchReqDTO;
import com.teach.gram.dto.req.post.PostReqDTO;
import com.teach.gram.dto.res.post.PostResDTO;
import com.teach.gram.model.Post;
import com.teach.gram.model.User;
import com.teach.gram.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostResDTO createPost(PostReqDTO dto) {

        if (dto.title().isEmpty())
            throw new RuntimeException("Title cannot be empty");

        Post post = new Post();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        post.setTitle(dto.title());
        post.setDescription(dto.description());
        post.setPhotoLink(dto.photoLink());
        post.setVideoLink(dto.videoLink());
        post.setUser(user);

        postRepository.save(post);

        return new PostResDTO(post.getId(), post.getTitle(), post.getDescription(), post.getPhotoLink(), post.getVideoLink());
    }

    public List<PostResDTO> getAllMyPosts() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Post> posts = postRepository.findByUser(user);

        return posts.stream()
                .map(post -> new PostResDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getPhotoLink(),
                        post.getVideoLink()
                ))
                .toList();
    }

    public List<PostResDTO> getPublicAndActivePostsByUserId(Long userId) {
        
        List<Post> posts = postRepository.findByUserIdAndPrivatePostFalseAndDeletedFalse(userId);

        return posts.stream()
                .map(post -> new PostResDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getPhotoLink(),
                        post.getVideoLink()
                ))
                .toList();
    }

    public PostResDTO getTaskById(Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return new PostResDTO(post.getId(), post.getTitulo(), post.getDescricao(), post.getStatus());
    }

    public PostResDTO updateTitleOrStatus(Long id, PostPatchReqDTO postPatchReqDTO) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        post.setDescricao(postPatchReqDTO.descricao());

        postRepository.save(post);

        return new PostResDTO(post.getId(), post.getTitulo(), post.getDescricao());
    }

    public void delete(Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        postRepository.delete(post);
    }
}

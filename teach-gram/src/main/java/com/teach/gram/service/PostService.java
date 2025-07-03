package com.teach.gram.service;

import com.teach.gram.dto.req.post.PostPatchReqDTO;
import com.teach.gram.dto.req.post.PostReqDTO;
import com.teach.gram.dto.res.PostResDTO;
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

    public PostResDTO createTask(PostReqDTO dto) {

        if (dto.titulo() == null)
            throw new RuntimeException("Title cannot be null");

        if (dto.descricao() == null)
            throw new RuntimeException("Description cannot be null");

        if (dto.status() == null)
            throw new RuntimeException("Status cannot be null");


        if (dto.titulo().isEmpty())
            throw new RuntimeException("Title cannot be empty");

        if (dto.descricao().isEmpty())
            throw new RuntimeException("Description cannot be empty");

        Post post = new Post();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        post.setTitulo(dto.titulo());
        post.setDescricao(dto.descricao());
        post.setStatus(dto.status());
        post.setUsuario(user);

        boolean response = postRepository.existsByTituloAndUsuario(post.getTitulo(), user);

        if (response) {
            throw new RuntimeException("Task already exists");
        }

        postRepository.save(post);

        return new PostResDTO(post.getId(), post.getTitulo(), post.getDescricao(), post.getStatus());
    }

    public List<PostResDTO> getAllTasks() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Post> posts = postRepository.findByUsuario(user);

        return posts.stream()
                .map(post -> new PostResDTO(
                        post.getId(),
                        post.getTitulo(),
                        post.getDescricao(),
                        post.getStatus()
                ))
                .toList();
    }

    public PostResDTO getTaskById(Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUsuario(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return new PostResDTO(post.getId(), post.getTitulo(), post.getDescricao(), post.getStatus());
    }

    public PostResDTO updateTitleOrStatus(Long id, PostPatchReqDTO postPatchReqDTO) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUsuario(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        post.setDescricao(postPatchReqDTO.descricao());

        postRepository.save(post);

        return new PostResDTO(post.getId(), post.getTitulo(), post.getDescricao());
    }

    public void delete(Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUsuario(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        postRepository.delete(post);
    }
}

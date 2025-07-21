package com.teach.gram.service;

import com.teach.gram.dto.req.post.PostPatchReqDTO;
import com.teach.gram.dto.req.post.PostReqDTO;
import com.teach.gram.dto.res.post.PostTimeAndUserResDTO;
import com.teach.gram.dto.res.post.PostTimeResDTO;
import com.teach.gram.dto.res.user.UserResDTO;
import com.teach.gram.model.Post;
import com.teach.gram.model.User;
import com.teach.gram.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostTimeResDTO createPost(PostReqDTO dto) {

        if (dto.title().isEmpty())
            throw new RuntimeException("Título não pode estar em branco");

        Post post = new Post();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        post.setTitle(dto.title());
        post.setDescription(dto.description());
        post.setPhotoLink(dto.photoLink());
        post.setVideoLink(dto.videoLink());
        post.setPrivatePost(dto.privatePost());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);

        return new PostTimeResDTO(post.getId(), post.getTitle(), post.getDescription(), post.getPhotoLink(), post.getVideoLink(), post.getLikes(), post.getPrivatePost(), post.getCreatedAt());
    }

    public List<PostTimeResDTO> getAllMyPosts() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Post> posts = postRepository.findByUserAndDeletedFalse(user);

        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> new PostTimeResDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getPhotoLink(),
                        post.getVideoLink(),
                        post.getLikes(),
                        post.getPrivatePost(),
                        post.getCreatedAt()
                ))
                .toList();
    }

    public List<PostTimeResDTO> getPublicAndActivePostsByUserId(Long userId) {
        
        List<Post> posts = postRepository.findByUserIdAndPrivatePostFalseAndDeletedFalse(userId);

        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> new PostTimeResDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getPhotoLink(),
                        post.getVideoLink(),
                        post.getLikes(),
                        post.getPrivatePost(),
                        post.getCreatedAt()
                ))
                .toList();
    }

    public List<PostTimeAndUserResDTO> getRecentPublicAndActivePosts() {

        List<Post> posts = postRepository.findByPrivatePostFalseAndDeletedFalse();

        return posts.stream()
                .filter(post -> !post.getUser().getDeleted())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(10)
                .map(post -> {
                    User user = post.getUser();

                    UserResDTO userDto = new UserResDTO(
                            user.getId(),
                            user.getName(),
                            user.getMail(),
                            user.getUsername(),
                            user.getDescription(),
                            user.getPhone(),
                            user.getProfileLink()
                    );

                    return new PostTimeAndUserResDTO(
                            post.getId(),
                            post.getTitle(),
                            post.getDescription(),
                            post.getPhotoLink(),
                            post.getVideoLink(),
                            post.getLikes(),
                            post.getCreatedAt(),
                            userDto
                    );
                })
                .toList();
    }

    public PostTimeResDTO getMyPostById(Long id) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUser(id, userAuth)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        return new PostTimeResDTO(post.getId(), post.getTitle(), post.getDescription(), post.getPhotoLink(), post.getVideoLink(), post.getLikes(), post.getPrivatePost(), post.getCreatedAt());
    }

    public void updatePostLikes(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));


        post.setLikes(post.getLikes() + 1);
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);
    }

    public PostTimeResDTO updatePost(Long id, PostPatchReqDTO postPatchReqDTO) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUser(id, userAuth)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        post.setTitle(postPatchReqDTO.title());
        post.setDescription(postPatchReqDTO.description());
        post.setPhotoLink(postPatchReqDTO.photoLink());
        post.setVideoLink(postPatchReqDTO.videoLink());
        post.setPrivatePost(postPatchReqDTO.privatePost());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);

        return new PostTimeResDTO(post.getId(), post.getTitle(), post.getDescription(), post.getPhotoLink(), post.getVideoLink(), post.getLikes(), post.getPrivatePost(), post.getCreatedAt());
    }

    public void deletePost(Long id) {

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findByIdAndUser(id, userAuth)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        post.setDeleted(true);
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);
    }
}

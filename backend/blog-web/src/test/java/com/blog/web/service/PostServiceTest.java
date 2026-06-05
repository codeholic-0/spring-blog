package com.blog.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.blog.common.dto.PostRequest;
import com.blog.common.dto.PostResponse;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.core.service.PostService;
import com.blog.persistence.entity.Post;
import com.blog.persistence.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private PostRequest createSampleRequest() {
        PostRequest req = new PostRequest();
        req.setTitle("Test Title");
        req.setContent("Test Content");
        req.setAuthor("Test Author");
        return req;
    }

    private Post createSamplePost(Long id) {
        Post post = new Post();
        post.setId(id);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setAuthor("Test Author");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return post;
    }

    @Test
    void createPost_ShouldReturnPostResponse() {
        PostRequest req = createSampleRequest();
        Post savedPost = createSamplePost(1L);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostResponse result = postService.createPost(req);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Title", result.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void getAllPosts_ShouldReturnList() {
        when(postRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(createSamplePost(1L), createSamplePost(2L)));

        List<PostResponse> result = postService.getAllPosts();

        assertEquals(2, result.size());
        verify(postRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getPostById_WhenPostExists_ShouldReturnPost() {
        Post post = createSamplePost(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostResponse result = postService.getPostById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    void getPostById_WhenPostNotFound_ShouldThrowException() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(99L));
    }

    @Test
    void updatePost_WhenPostExists_ShouldReturnUpdatedPost() {
        Post existingPost = createSamplePost(1L);
        PostRequest req = createSampleRequest();
        req.setTitle("Updated Title");

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        PostResponse result = postService.updatePost(1L, req);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void updatePost_WhenPostNotFound_ShouldThrowException() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(99L, createSampleRequest()));
    }

    @Test
    void deletePost_WhenPostExists_ShouldDelete() {
        Post post = createSamplePost(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L);

        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void deletePost_WhenPostNotFound_ShouldThrowException() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.deletePost(99L));
    }
}

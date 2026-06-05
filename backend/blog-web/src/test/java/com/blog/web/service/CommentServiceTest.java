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

import com.blog.common.dto.CommentRequest;
import com.blog.common.dto.CommentResponse;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.core.service.CommentService;
import com.blog.persistence.entity.Comment;
import com.blog.persistence.entity.Post;
import com.blog.persistence.repository.CommentRepository;
import com.blog.persistence.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

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

    private CommentRequest createSampleRequest() {
        CommentRequest req = new CommentRequest();
        req.setBody("Test Body");
        req.setAuthor("Test Author");
        return req;
    }

    private Comment createSampleComment(Long id, Post post) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setBody("Test Body");
        comment.setAuthor("Test Author");
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }

    @Test
    void createComment_WhenPostExists_ShouldReturnCommentResponse() {
        Post post = createSamplePost(1L);
        CommentRequest req = createSampleRequest();
        Comment savedComment = createSampleComment(1L, post);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentResponse result = commentService.createComment(1L, req);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Body", result.getBody());
        assertEquals("Test Author", result.getAuthor());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_WhenPostNotFound_ShouldThrowException() {
        CommentRequest req = createSampleRequest();
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(99L, req));
    }

    @Test
    void getCommentsByPostId_ShouldReturnList() {
        Post post = createSamplePost(1L);
        when(commentRepository.findByPostIdOrderByCreatedAtAsc(1L))
                .thenReturn(List.of(createSampleComment(1L, post), createSampleComment(2L, post)));

        List<CommentResponse> result = commentService.getCommentsByPostId(1L);

        assertEquals(2, result.size());
        verify(commentRepository, times(1)).findByPostIdOrderByCreatedAtAsc(1L);
    }

    @Test
    void deleteComment_WhenCommentExists_ShouldDelete() {
        Post post = createSamplePost(1L);
        Comment comment = createSampleComment(1L, post);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_WhenCommentNotFound_ShouldThrowException() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(99L));
    }
}

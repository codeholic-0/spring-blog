package com.blog.web.controller;

import com.blog.common.dto.CommentRequest;
import com.blog.common.dto.CommentResponse;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.core.service.CommentService;
import com.blog.web.exception.GlobalExceptionHandler;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private CommentResponse createSampleResponse(Long id) {
        CommentResponse res = new CommentResponse();
        res.setId(id);
        res.setBody("Test Body");
        res.setAuthor("Test Author");
        res.setCreatedAt(LocalDateTime.now());
        return res;
    }

    @Test
    void postComment_ShouldReturn201() throws Exception {
        when(commentService.createComment(eq(1L), any(CommentRequest.class)))
                .thenReturn(createSampleResponse(1L));

        CommentRequest req = new CommentRequest();
        req.setBody("Test Body");
        req.setAuthor("Test Author");
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.body").value("Test Body"));
    }

    @Test
    void postComment_WhenPostNotFound_ShouldReturn404() throws Exception {
        when(commentService.createComment(eq(99L), any(CommentRequest.class)))
                .thenThrow(new ResourceNotFoundException("Post", 99L));

        CommentRequest req = new CommentRequest();
        req.setBody("Test Body");
        req.setAuthor("Test Author");
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/posts/99/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCommentsByPostId_ShouldReturn200() throws Exception {
        when(commentService.getCommentsByPostId(1L))
                .thenReturn(List.of(createSampleResponse(1L), createSampleResponse(2L)));

        mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteComment_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/posts/1/comments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteComment_WhenNotFound_ShouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Comment", 99L))
                .when(commentService).deleteComment(99L);

        mockMvc.perform(delete("/api/posts/1/comments/99"))
                .andExpect(status().isNotFound());
    }
}

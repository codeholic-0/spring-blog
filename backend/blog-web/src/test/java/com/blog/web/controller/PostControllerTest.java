package com.blog.web.controller;

import com.blog.common.dto.PostRequest;
import com.blog.common.dto.PostResponse;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.core.service.PostService;
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
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private PostResponse createSampleResponse(Long id) {
        PostResponse res = new PostResponse();
        res.setId(id);
        res.setTitle("Test Title");
        res.setContent("Test Content");
        res.setAuthor("Test Author");
        res.setCreatedAt(LocalDateTime.now());
        res.setUpdatedAt(LocalDateTime.now());
        return res;
    }

    @Test
    void addPost_ShouldReturn201() throws Exception {
        PostResponse response = createSampleResponse(1L);
        when(postService.createPost(any(PostRequest.class))).thenReturn(response);

        PostRequest req = new PostRequest();
        req.setTitle("Test Title");
        req.setContent("Test Content");
        req.setAuthor("Test Author");
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void getAllPosts_ShouldReturn200() throws Exception {
        when(postService.getAllPosts()).thenReturn(List.of(createSampleResponse(1L), createSampleResponse(2L)));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getPostById_ShouldReturn200() throws Exception {
        when(postService.getPostById(1L)).thenReturn(createSampleResponse(1L));

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getPostById_WhenNotFound_ShouldReturn404() throws Exception {
        when(postService.getPostById(99L)).thenThrow(new ResourceNotFoundException("Post", 99L));

        mockMvc.perform(get("/api/posts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePost_ShouldReturn200() throws Exception {
        PostResponse response = createSampleResponse(1L);
        response.setTitle("Updated Title");
        when(postService.updatePost(eq(1L), any(PostRequest.class))).thenReturn(response);

        PostRequest req = new PostRequest();
        req.setTitle("Updated Title");
        req.setContent("Test Content");
        req.setAuthor("Test Author");
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void deletePost_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePost_WhenNotFound_ShouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Post", 99L)).when(postService).deletePost(99L);

        mockMvc.perform(delete("/api/posts/99"))
                .andExpect(status().isNotFound());
    }
}

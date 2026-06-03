package com.blog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.common.dto.PostRequest;
import com.blog.common.dto.PostResponse;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.persistence.entity.Post;
import com.blog.persistence.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    private PostResponse toResponse(Post post) {
        PostResponse res = new PostResponse();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setAuthor(post.getAuthor());
        res.setCreatedAt(post.getCreatedAt());
        res.setUpdatedAt(post.getUpdatedAt());
        return res;
    }

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(PostRequest req) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setAuthor(req.getAuthor());

        Post savedPost = postRepository.save(post);
        return toResponse(savedPost);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        return toResponse(post);
    }

    public PostResponse updatePost(Long postId, PostRequest req) {
        Post originalPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        originalPost.setTitle(req.getTitle());
        originalPost.setContent(req.getContent());
        originalPost.setAuthor(req.getAuthor());
        Post updatedPost = postRepository.save(originalPost);
        return toResponse(updatedPost);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        postRepository.delete(post);
    }

}

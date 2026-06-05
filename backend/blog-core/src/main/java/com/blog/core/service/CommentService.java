package com.blog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.common.dto.CommentRequest;
import com.blog.common.dto.CommentResponse;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.persistence.entity.Comment;
import com.blog.persistence.entity.Post;
import com.blog.persistence.repository.CommentRepository;
import com.blog.persistence.repository.PostRepository;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private CommentResponse toCommentResponse(Comment comment) {
        CommentResponse res = new CommentResponse();
        res.setId(comment.getId());
        res.setAuthor(comment.getAuthor());
        res.setBody(comment.getBody());
        res.setCreatedAt(comment.getCreatedAt());
        return res;
    }

    private Comment toCommentEntity(CommentRequest req, Post post) {
        Comment comment = new Comment();
        comment.setAuthor(req.getAuthor());
        comment.setBody(req.getBody());
        comment.setPost(post);

        return comment;
    }

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse createComment(Long postId, CommentRequest req) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        Comment comment = toCommentEntity(req, post);
        Comment savedComment = commentRepository.save(comment);
        return toCommentResponse(savedComment);
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream().map(this::toCommentResponse).toList();
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
        commentRepository.delete(comment);
    }
}

package com.blog.common.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private String body;

    @NotBlank
    private String author;

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}

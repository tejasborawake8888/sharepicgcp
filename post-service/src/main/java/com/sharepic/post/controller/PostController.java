package com.sharepic.post.controller;

import com.sharepic.post.entity.Posts;
import com.sharepic.post.model.PostRequest;
import com.sharepic.post.service.PostService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Posts> createPost(
            @AuthenticationPrincipal UUID userId,
            @Valid @ModelAttribute PostRequest request
            ){
        log.info("Post creation started");
        return ResponseEntity.ok().body(postService.createPost(userId, request.getCaption(), request.getImage()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Posts> getPost(@PathVariable UUID postId){
        log.info("getting post started");
        return ResponseEntity.ok().body(postService.getPost(postId));
    }

    @PutMapping(value = "/{postId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Posts> updatePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal UUID userId,
            @Valid @ModelAttribute PostRequest request
    ){
        log.info("Updating the post started");
        return ResponseEntity.ok().body(postService.updatePost(postId,userId, request.getCaption(), request.getImage()));
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal UUID userId
    ){
        log.info("Post deletion started");
        postService.deletePost(postId,userId);
    }
}

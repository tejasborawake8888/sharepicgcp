package com.sharepic.feed.controller;

import com.sharepic.feed.model.PostResponse;
import com.sharepic.feed.service.FeedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getFeed(
            HttpServletRequest request) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<PostResponse> posts = feedService.getFeed(authorization);
        System.out.println("Authorization header present: "
                + (authorization != null && authorization.startsWith("Bearer ")));
        return ResponseEntity.ok(posts);
    }
}

package com.sharepic.feed.client;

import com.sharepic.feed.entity.Posts;
import com.sharepic.feed.model.PostResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
public class PostServiceClient {

    private final RestClient restClient;

    public PostServiceClient(
            RestClient.Builder builder,
            @Value("${post-service.base-url}") String baseUrl
    ){
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public List<PostResponse> getAllPosts(String authorization) {
        return restClient.get()
                .uri("/api/v1/posts/posts")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PostResponse>>() {});
    }

    public PostResponse getSinglePost(String authorization, UUID postId) {
        return restClient.get()
                .uri("/api/v1/posts/{postId}",postId)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PostResponse.class);
    }
}

package com.sharepic.feed.service;

import com.sharepic.feed.client.PostServiceClient;
import com.sharepic.feed.model.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    private final PostServiceClient postServiceClient;

    public FeedService(PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    public List<PostResponse> getFeed(String authorization) {
        return postServiceClient.getAllPosts(authorization);
    }
}

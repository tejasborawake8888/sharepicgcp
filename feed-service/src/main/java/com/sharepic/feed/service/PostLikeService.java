package com.sharepic.feed.service;

import com.sharepic.feed.client.PostServiceClient;
import com.sharepic.feed.entity.PostLike;
import com.sharepic.feed.entity.PostLikeId;
import com.sharepic.feed.model.PostResponse;
import com.sharepic.feed.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostServiceClient postServiceClient;

    public PostLike likePost(UUID userId, UUID postId, String authorization) {

        // 1. Verify that the post exists
        PostResponse post = postServiceClient.getSinglePost(authorization, postId);

        // 2. Prevent users from liking their own posts
        if (post.userId().equals(userId)) {
            log.warn("Like rejected: user {} attempted to like their own post {}",
                    userId, postId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot like your own post"
            );
        }

        // 3. Build the composite key
        PostLikeId likeId = new PostLikeId(postId, userId);

        // 4. Prevent duplicate likes
        if (postLikeRepository.existsById(likeId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You have already liked this post"
            );
        }

        // 5. Save the like
        PostLike postLike = new PostLike();
        postLike.setId(likeId);
        log.info("User {} liked post {}", userId, postId);
        return postLikeRepository.save(postLike);
    }

}

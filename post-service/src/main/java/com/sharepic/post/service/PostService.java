package com.sharepic.post.service;

import com.sharepic.post.entity.Post;
import com.sharepic.post.enums.ErrorCode;
import com.sharepic.post.exception.IllegalUserForPostUpdateException;
import com.sharepic.post.exception.PostNotFoundException;
import com.sharepic.post.repository.PostRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(UUID userId, String caption, String imageURL){
        Instant now = Instant.now();

        Post post = Post.builder()
                .userId(userId)
                .caption(caption)
                .impageUrl(imageURL)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return postRepository.save(post);
    }

    public Post getPost(UUID postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND.getMsg()));
    }

    public Post updatePost(UUID postId, UUID userId,String caption, String imageURL){
        Post post = getPost(postId);
        if(!post.getUserId().equals(userId)){
            throw new IllegalUserForPostUpdateException(ErrorCode.ILLEGAL_USER_FOR_POST_UPDATE.getMsg());
        }
        post.setCaption(caption);
        post.setImpageUrl(imageURL);
        post.setUpdatedAt(Instant.now());
        return postRepository.save(post);
    }

    public void deletePost(UUID postId, UUID userId){
        Post post = getPost(postId);
        if (!post.getUserId().equals(userId)) {
            throw new IllegalUserForPostUpdateException(
                     ErrorCode.ILLEGAL_USER_FOR_POST_UPDATE.getMsg());
        }
        postRepository.delete(post);
    }
}

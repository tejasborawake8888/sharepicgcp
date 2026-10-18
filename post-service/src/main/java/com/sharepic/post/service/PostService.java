package com.sharepic.post.service;

import com.sharepic.post.entity.Posts;
import com.sharepic.post.enums.ErrorCode;
import com.sharepic.post.exception.IllegalUserForPostUpdateException;
import com.sharepic.post.exception.PostNotFoundException;
import com.sharepic.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageStorageService postImageStorageService;

    public Posts createPost(UUID userId, String caption, MultipartFile image){
        Instant now = Instant.now();
        Posts post = Posts.builder()
                .userId(userId)
                .caption(caption)
                .createdAt(now)
                .updatedAt(now)
                .build();

        if(image != null && !image.isEmpty()){
            String imageUrl = postImageStorageService.uploadPostImage(image);
            post.setImageUrl(imageUrl);
        }

        return postRepository.save(post);
    }

    public Posts getPost(UUID postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND.getMsg()));
    }

    public Posts updatePost(UUID postId, UUID userId,String caption, MultipartFile image){
        Posts post = getPost(postId);
        if(!post.getUserId().equals(userId)){
            throw new IllegalUserForPostUpdateException(ErrorCode.ILLEGAL_USER_FOR_POST_UPDATE.getMsg());
        }
        post.setCaption(caption);

        if(image != null && !image.isEmpty()){
            String imageUrl = postImageStorageService.uploadPostImage(image);
            post.setImageUrl(imageUrl);
        }

        post.setUpdatedAt(Instant.now());
        return postRepository.save(post);
    }

    public void deletePost(UUID postId, UUID userId){
        Posts post = getPost(postId);
        if (!post.getUserId().equals(userId)) {
            throw new IllegalUserForPostUpdateException(
                     ErrorCode.ILLEGAL_USER_FOR_POST_UPDATE.getMsg());
        }
        postRepository.delete(post);
    }

    public List<Posts> getAllPosts(){
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
}

package com.sharepic.post.repository;

import com.sharepic.post.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Posts, UUID> {

    List<Posts> findAllByOrderByCreatedAtDesc();

}

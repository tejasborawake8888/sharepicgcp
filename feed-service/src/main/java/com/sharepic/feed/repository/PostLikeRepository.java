package com.sharepic.feed.repository;

import com.sharepic.feed.entity.PostLike;
import com.sharepic.feed.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
}

package com.sharepic.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id",nullable = false)
    private UUID userId;

    @Column(nullable = false,length = 2000)
    private String caption;

    @Column(name = "image_url")
    private String impageUrl;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}

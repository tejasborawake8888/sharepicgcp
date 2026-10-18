package com.sharepic.feed.model;

import java.time.Instant;
import java.util.UUID;

public record PostResponse(UUID id,
                           UUID userId,
                           String caption,
                           String imageUrl,
                           Instant createdAt,
                           Instant updatedAt) {
}

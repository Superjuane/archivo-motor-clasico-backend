package com.jolivan.archivomotorclasicobackend.Upvotes.Entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Upvote {
    @EmbeddedId
    private UpvoteEmbeddedID upvoteId;
}

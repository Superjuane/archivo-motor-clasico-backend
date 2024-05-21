package com.jolivan.archivomotorclasicobackend.Upvotes.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpvoteEmbeddedID implements Serializable {
    @Column(name = "my_user")
    private long userId;
    @Column(name = "comment")
    private long commentId;
}

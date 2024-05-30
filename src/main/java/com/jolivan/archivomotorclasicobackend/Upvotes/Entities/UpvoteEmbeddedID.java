package com.jolivan.archivomotorclasicobackend.Upvotes.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpvoteEmbeddedID implements Serializable {
    @Column(name = "my_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private long userId;
    @Column(name = "comment")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private long commentId;
}

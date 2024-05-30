package com.jolivan.archivomotorclasicobackend.Comments.Entities;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment{
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    String text;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    MyUser creator;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    Comment commentParent;

    String resourceId;
}

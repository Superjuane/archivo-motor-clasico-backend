package com.jolivan.archivomotorclasicobackend.Comments.Entities;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    MyUser creator;

    @OneToOne
    Comment commentParent;

    String resourceId;
}

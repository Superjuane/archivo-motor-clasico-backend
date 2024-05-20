package com.jolivan.archivomotorclasicobackend.Comments.Entities;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;


@Data
@JsonComponent
public class CommentResponseDTO {
    Long id;
    String text;
    String creator;
    CommentResponseDTO commentParent;
    String resourceId;
}

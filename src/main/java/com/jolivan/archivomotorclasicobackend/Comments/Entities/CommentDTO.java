package com.jolivan.archivomotorclasicobackend.Comments.Entities;

import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

@Data
@JsonComponent
public class CommentDTO {
    String text;
    Long commentParentId;
    String resourceId;
}

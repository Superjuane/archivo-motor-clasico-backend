package com.jolivan.archivomotorclasicobackend.Upvotes.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@Data
@JsonComponent
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpvoteResponseDTO {
    int upvotes;
    List<String> users;

}

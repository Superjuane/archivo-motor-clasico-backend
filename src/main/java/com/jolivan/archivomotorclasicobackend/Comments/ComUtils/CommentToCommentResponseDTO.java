package com.jolivan.archivomotorclasicobackend.Comments.ComUtils;

import com.jolivan.archivomotorclasicobackend.Comments.Entities.Comment;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.CommentResponseDTO;

public class CommentToCommentResponseDTO {
    private CommentToCommentResponseDTO() {
        throw new IllegalStateException("Utility class");
    }

    public static CommentResponseDTO toCommentResponseDTO(Comment comment) {
        CommentResponseDTO response = new CommentResponseDTO();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setCreator(comment.getCreator().getUsername());
        //TODO: revisar esta recursiva !!
        if(comment.getCommentParent() != null )  response.setCommentParent(toCommentResponseDTO(comment.getCommentParent()));
        else if (comment.getResourceId() != null) response.setResourceId(comment.getResourceId());

        return response;
    }
}

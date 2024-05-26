package com.jolivan.archivomotorclasicobackend.Comments.Controllers;

import com.jolivan.archivomotorclasicobackend.Comments.Controllers.ExceptionControl.Exceptions.CommentNotFoundException;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.Comment;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.CommentDTO;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.CommentResponseDTO;
import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserForbiddenException;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CommentsController {

    private final String URL = "http://localhost:3000";
    private final CommentsService commentsService;

    @Autowired
    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/comments")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> getComments(@RequestParam(name="resource", required = false) String resourceId, @RequestParam(name="comment",required = false) Long commentParentId) {
        Map<String, String>  body = new HashMap<>();

        if(resourceId == null && commentParentId == null) {
            body.put("message", "Comments must have a resourceId or a commentParentId");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        if(resourceId != null && commentParentId != null) {
            body.put("message", "Comments cannot have both a resourceId and a commentParentId");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        List<CommentResponseDTO> comments = commentsService.getComments(resourceId, commentParentId);

        //TODO: refactor this
        if(resourceId != null){

            for(CommentResponseDTO comment : comments){
                List<CommentResponseDTO> auxComments = commentsService.getComments(null, comment.getId());
                List<Long> childrenComments = auxComments.stream().map(CommentResponseDTO::getId).toList();
                comment.setComments(childrenComments);
            }
        }

        if(comments == null) {
            body.put("message", "Error getting comments");
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/comments")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> createComment(@RequestBody CommentDTO commentDTO) {
        Map<String, String> body = new HashMap<>();


        if(commentDTO.getResourceId() == null && commentDTO.getCommentParentId() == null) {
            body.put("message", "Comment must have a resourceId or a commentParentId");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        if(commentDTO.getResourceId() != null && commentDTO.getCommentParentId() != null) {
            body.put("message", "Comment cannot have both a resourceId and a commentParentId");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        CommentResponseDTO commentResponse = commentsService.createComment(commentDTO);

        if(commentResponse == null) {
            body.put("message", "Error creating comment");
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    //TODO: PUT para los upvotes, no para modificar un comentario !
//    @PutMapping("/comments/{id}")


    @DeleteMapping("/comments/{id}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> deleteComment(@PathVariable Long id) {
        Map<String, String> body = new HashMap<>();

        if(id == null) {
            body.put("message", "Comment id is required");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        boolean deleted;

        try {
            deleted = commentsService.deleteComment(id);
        } catch (UserForbiddenException e) {
            body.put("message", "User not allowed to delete this comment");
            return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
        } catch (CommentNotFoundException e){
            body.put("message", "Comment not found");
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            body.put("message", "Error deleting comment");
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!deleted) {
            body.put("message", "Error deleting comment");
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

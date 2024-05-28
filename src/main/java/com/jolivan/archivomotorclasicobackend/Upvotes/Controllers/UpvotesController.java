package com.jolivan.archivomotorclasicobackend.Upvotes.Controllers;

import com.jolivan.archivomotorclasicobackend.Comments.Controllers.ExceptionControl.Exceptions.CommentNotFoundException;
import com.jolivan.archivomotorclasicobackend.Upvotes.Controllers.ExceptionControl.Exceptions.UserAlredyUpvotedException;
import com.jolivan.archivomotorclasicobackend.Upvotes.Controllers.ExceptionControl.Exceptions.UserHasntUpvotedYetException;
import com.jolivan.archivomotorclasicobackend.Upvotes.Entities.UpvoteResponseDTO;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UpvotesController {

    private final String URL = "http://localhost:3000";
    private final UpvotesService upvotesService;

    @Autowired
    public UpvotesController(UpvotesService upvotesService) {
        this.upvotesService = upvotesService;
    }

    @GetMapping("/upvotes/{commentId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> getUpvotesByCommentId(@PathVariable Long commentId) {
        Map<String, String> body = new HashMap<>();
        UpvoteResponseDTO upvotes;

        try {
            upvotes = upvotesService.getUpvotesByCommentId(commentId);
        } catch (CommentNotFoundException e) {
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        if(upvotes == null) {
            body.put("message", "No upvotes found for comment with id " + commentId);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(upvotes, HttpStatus.OK);
    }

    @PostMapping("/upvotes/{commentId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> upvoteComment(@PathVariable Long commentId) {
        Map<String, String> body = new HashMap<>();
        UpvoteResponseDTO upvotes;

        try {
            upvotes = upvotesService.upvoteComment(commentId);
        } catch (CommentNotFoundException | UserNodeNotFoundException e) {
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (UserAlredyUpvotedException e) {
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        if(upvotes == null) {
            body.put("message", "No upvotes found for comment with id " + commentId);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(upvotes, HttpStatus.OK);
    }

    @DeleteMapping("/upvotes/{commentId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> deleteUpvote(@PathVariable Long commentId) {
        Map<String, String> body = new HashMap<>();
        UpvoteResponseDTO upvotes;

        try {
            upvotes = upvotesService.deleteUpvote(commentId);
        } catch (CommentNotFoundException | UserNodeNotFoundException e) {
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (UserHasntUpvotedYetException e) {
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        if(upvotes == null) {
            body.put("message", "No upvotes found for comment with id " + commentId);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(upvotes, HttpStatus.NO_CONTENT);
    }

}

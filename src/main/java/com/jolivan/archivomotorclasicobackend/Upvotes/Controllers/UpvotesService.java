package com.jolivan.archivomotorclasicobackend.Upvotes.Controllers;

import com.jolivan.archivomotorclasicobackend.Comments.Controllers.CommentsService;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.CommentResponseDTO;
import com.jolivan.archivomotorclasicobackend.Security.SecUtils.Session;
import com.jolivan.archivomotorclasicobackend.Upvotes.Controllers.ExceptionControl.Exceptions.UserAlredyUpvotedException;
import com.jolivan.archivomotorclasicobackend.Upvotes.Controllers.ExceptionControl.Exceptions.UserHasntUpvotedYetException;
import com.jolivan.archivomotorclasicobackend.Upvotes.Entities.Upvote;
import com.jolivan.archivomotorclasicobackend.Upvotes.Entities.UpvoteEmbeddedID;
import com.jolivan.archivomotorclasicobackend.Upvotes.Entities.UpvoteResponseDTO;
import com.jolivan.archivomotorclasicobackend.User.Controllers.service.UserService;
import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpvotesService {

    private final UpvotesRepository upvotesRepository;
    private final CommentsService commentsService;

    private final UserService userService;

    @Autowired
    public UpvotesService(UpvotesRepository upvotesRepository, CommentsService commentsService, UserService userService) {
        this.upvotesRepository = upvotesRepository;
        this.commentsService = commentsService;
        this.userService = userService;
    }

    public UpvoteResponseDTO getUpvotesByCommentId(Long commentId) {
        MyUser user = userService.findUserByUsername(Session.getCurrentUserName());
        if(user == null) throw new UserNodeNotFoundException();

        commentsService.getCommentById(commentId); //THROWS CommentNotFoundException

        List<Upvote> upvotes = upvotesRepository.findByCommentId(commentId);

        boolean userUpvoted = upvotes.stream().anyMatch(upvote -> upvote.getUpvoteId().getUserId() == user.getId());

        UpvoteResponseDTO upvoteResponseDTO = UpvoteResponseDTO.builder()
                .upvotes(upvotes.size())
                .build();

        if(userUpvoted)
            upvoteResponseDTO.setUsers(List.of(user.getUsername()));


        return upvoteResponseDTO;
    }

    public UpvoteResponseDTO upvoteComment(Long commentId) {
        MyUser user = userService.findUserByUsername(Session.getCurrentUserName());
        if(user == null) throw new UserNodeNotFoundException();

        commentsService.getCommentById(commentId); //THROWS CommentNotFoundException

        List<Upvote> upvotes = upvotesRepository.findByCommentId(commentId);

        boolean userUpvoted = upvotes.stream().anyMatch(upvote -> upvote.getUpvoteId().getUserId() == user.getId());

        if(!userUpvoted){
            UpvoteEmbeddedID upvoteId = UpvoteEmbeddedID.builder()
                    .commentId(commentId)
                    .userId(user.getId())
                    .build();
            Upvote upvote = Upvote.builder()
                    .upvoteId(upvoteId)
                    .build();
            upvotesRepository.save(upvote);
        } else {
            throw new UserAlredyUpvotedException();
        }

        return UpvoteResponseDTO.builder()
                .upvotes(upvotes.size() + 1)
                .users(List.of(user.getUsername()))
                .build();
    }

    public UpvoteResponseDTO deleteUpvote(Long commentId) {
        MyUser user = userService.findUserByUsername(Session.getCurrentUserName());
        if(user == null) throw new UserNodeNotFoundException();

        commentsService.getCommentById(commentId); //THROWS CommentNotFoundException

        List<Upvote> upvotes = upvotesRepository.findByCommentId(commentId);

        boolean userUpvoted = upvotes.stream().anyMatch(upvote -> upvote.getUpvoteId().getUserId() == user.getId());

        if(userUpvoted){
            UpvoteEmbeddedID upvoteId = UpvoteEmbeddedID.builder()
                    .commentId(commentId)
                    .userId(user.getId())
                    .build();
            upvotesRepository.deleteById(upvoteId);
        } else {
            throw new UserHasntUpvotedYetException();
        }

        return UpvoteResponseDTO.builder()
                .upvotes(upvotes.size() - 1)
                .build();
    }
}

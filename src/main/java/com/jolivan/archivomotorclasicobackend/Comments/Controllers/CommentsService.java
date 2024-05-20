package com.jolivan.archivomotorclasicobackend.Comments.Controllers;

import com.jolivan.archivomotorclasicobackend.Comments.ComUtils.CommentToCommentResponseDTO;
import com.jolivan.archivomotorclasicobackend.Comments.Controllers.ExceptionControl.Exceptions.CommentNotFoundException;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.Comment;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.CommentDTO;
import com.jolivan.archivomotorclasicobackend.Comments.Entities.CommentResponseDTO;
import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ResourceNotFoundException;
import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ResourceRepository;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Security.SecUtils.Session;
import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserForbiddenException;
import com.jolivan.archivomotorclasicobackend.User.Controllers.service.UserService;
import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    private final ResourceRepository resourceRepository;
    private final UserService userService;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository, ResourceRepository resourceRepository, UserService userService) {
        this.commentsRepository = commentsRepository;
        this.resourceRepository = resourceRepository;
        this.userService = userService;
    }

    public CommentResponseDTO createComment(CommentDTO commentDTO) {
        MyUser user = userService.findUserByUsername(Session.getCurrentUserName());

        if(user == null) {
            throw new UserNodeNotFoundException();
        }

        Comment parentComment = null;
        Resource resource = null;

        if(commentDTO.getCommentParentId() != null) {
            parentComment = commentsRepository.findById(commentDTO.getCommentParentId()).orElse(null);
            if(parentComment == null) {
                throw new RuntimeException("Parent comment not found"); // TODO: Create exception
            }
        } else if (commentDTO.getResourceId() != null) {
            resource = resourceRepository.getResource(commentDTO.getResourceId());
            if(resource == null) {
                throw new ResourceNotFoundException();
            }
        }

        Comment comment = Comment.builder()
                .creator(user)
                .text(commentDTO.getText())
                .build();

        if(parentComment != null) {
            comment.setCommentParent(parentComment);
        } else if (resource != null) {
            comment.setResourceId(resource.getId());
        } else {
            throw new RuntimeException("Comment must have a resourceId or a commentParentId");
        }


        return CommentToCommentResponseDTO.toCommentResponseDTO(commentsRepository.save(comment));
    }

    public List<CommentResponseDTO> getComments(String resourceId, Long commentParentId) {
        List<Comment> comments;
        List<CommentResponseDTO> result = new ArrayList<>();

        if(resourceId != null) {
            comments =  commentsRepository.findByResourceId(resourceId);
        } else if (commentParentId != null) {
            comments = commentsRepository.findByCommentParentId(commentParentId);
        } else {
            throw new RuntimeException("Comments must have a resourceId or a commentParentId");
        }

        for(Comment comment : comments) {
           result.add(CommentToCommentResponseDTO.toCommentResponseDTO(comment));
        }

        return result;
    }

    public boolean deleteComment(Long id) {
        MyUser user = userService.findUserByUsername(Session.getCurrentUserName());
        if(user == null) {
            throw new UserNodeNotFoundException();
        }

        Comment comment = commentsRepository.findById(id).orElse(null);

        if(comment == null) {
            throw new CommentNotFoundException();
        }

        if(!comment.getCreator().getUsername().equals(user.getUsername())) {
            throw new UserForbiddenException();
        }

        try{
            commentsRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}

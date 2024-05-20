package com.jolivan.archivomotorclasicobackend.Comments.Controllers;

import com.jolivan.archivomotorclasicobackend.Comments.Entities.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CommentsRepository extends CrudRepository<Comment,Long>{
    List<Comment> findByResourceId(String resourceId);

    List<Comment> findByCommentParentId(Long commentParentId);
}

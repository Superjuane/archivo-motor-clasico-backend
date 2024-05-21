package com.jolivan.archivomotorclasicobackend.Upvotes.Controllers;

import com.jolivan.archivomotorclasicobackend.Upvotes.Entities.Upvote;
import com.jolivan.archivomotorclasicobackend.Upvotes.Entities.UpvoteEmbeddedID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpvotesRepository extends CrudRepository<Upvote, UpvoteEmbeddedID>{
    @Query("SELECT u FROM Upvote u WHERE u.upvoteId.commentId = ?1")
    List<Upvote> findByCommentId(Long commentId);
}

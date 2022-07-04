package com.hurui.service;

import com.hurui.entity.Comment;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;

import java.util.List;

public interface CommentService {
    Uni<List<Comment>> listComments(Integer limit);
    Uni<List<Comment>> listCommentsByPostId(Long postId);
    Uni<Comment> createComment(Long postId, Comment comment);
    Uni<Void> deleteComment(Long id);
    Uni<JsonArray> getCommentChangelog(Long id);
}

package com.hurui.service;

import com.hurui.entity.Comment;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface CommentService {
    Uni<List<Comment>> listComments(Integer limit);
    Uni<Comment> createComments(Comment comment);
}

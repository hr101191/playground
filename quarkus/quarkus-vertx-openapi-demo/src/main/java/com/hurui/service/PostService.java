package com.hurui.service;

import com.hurui.entity.Post;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface PostService {
    Uni<List<Post>> listPosts(Integer limit);
    Uni<Post> createPosts(Post post);
}

package com.hurui.service;

import com.hurui.entity.Post;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;

import java.util.List;

public interface PostService {
    Uni<List<Post>> listPosts(Integer limit);
    Uni<Post> createPost(Post post);
    Uni<Post> updatePost(Post post);
    Uni<Void> deletePost(Long id);
    Uni<JsonArray> getPostChangelog(Long id);
}

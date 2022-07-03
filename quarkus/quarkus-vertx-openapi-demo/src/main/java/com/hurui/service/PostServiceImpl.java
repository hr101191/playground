package com.hurui.service;

import com.hurui.entity.Post;
import com.hurui.repository.PostRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private Vertx vertx;
    private final PostRepository postRepository;
    private final EntityManagerFactory entityManagerFactory;

    public PostServiceImpl(Vertx vertx, PostRepository postRepository, EntityManagerFactory entityManagerFactory) {
        this.vertx = vertx;
        this.postRepository = postRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Uni<List<Post>> listPosts(Integer limit) {
        return Uni.createFrom().<List<Post>>emitter(uniEmitter -> {
                    try {
                        Optional<Integer> limitOptional = Optional.ofNullable(limit);
                        if(limitOptional.isPresent()) {
                            uniEmitter.complete(
                                    this.postRepository.findAll()
                                            .stream()
                                            .limit(limitOptional.get())
                                            .collect(Collectors.toList())
                            );
                        } else {
                            uniEmitter.complete(
                                    this.postRepository.findAll()
                                            .stream()
                                            .collect(Collectors.toList())
                            );
                        }
                    } catch (Exception ex) {
                        uniEmitter.fail(ex);
                    }
                });
    }

    @Override
    public Uni<Post> createPosts(Post post) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                this.postRepository.persist(post);
                uniEmitter.complete(post);
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    public Uni<List<Post>> getPostChangelog(Long id) {
        return Uni.createFrom().<List<Post>>emitter(uniEmitter -> {
            try {
                uniEmitter.complete(
                        this.postRepository.findAll()
                                .stream()
                                .collect(Collectors.toList())
                );
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }

    private List<Post> getPostChangelogInternal(Long id) {
        return null;
    }
}

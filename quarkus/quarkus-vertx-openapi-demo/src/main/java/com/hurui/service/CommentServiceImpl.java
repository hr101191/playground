package com.hurui.service;

import com.hurui.entity.Comment;
import com.hurui.repository.CommentRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private Vertx vertx;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(Vertx vertx, CommentRepository commentRepository) {
        this.vertx = vertx;
        this.commentRepository = commentRepository;
    }

    @Override
    public Uni<List<Comment>> listComments(Integer limit) {
        return Uni.createFrom().<List<Comment>>emitter(uniEmitter -> {
            try {
                Optional<Integer> limitOptional = Optional.ofNullable(limit);
                if(limitOptional.isPresent()) {
                    uniEmitter.complete(
                            this.commentRepository.findAll()
                                    .stream()
                                    .limit(limitOptional.get())
                                    .collect(Collectors.toList())
                    );
                } else {
                    uniEmitter.complete(
                            this.commentRepository.findAll()
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
    public Uni<Comment> createComments(Comment comment) {
        return Uni.createFrom().emitter(uniEmitter -> {
            try {
                this.commentRepository.persist(comment);
                uniEmitter.complete(comment);
            } catch (Exception ex) {
                uniEmitter.fail(ex);
            }
        });
    }
}

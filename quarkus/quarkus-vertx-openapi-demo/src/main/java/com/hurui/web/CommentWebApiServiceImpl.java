package com.hurui.web;

import com.hurui.entity.Comment;
import com.hurui.entity.Post;
import com.hurui.service.CommentService;
import com.hurui.service.PostService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.smallrye.mutiny.Uni;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
@Transactional
public class CommentWebApiServiceImpl implements CommentWebApiService{

    private static final Logger logger = LoggerFactory.getLogger(CommentWebApiServiceImpl.class);

    private final Vertx vertx;
    private final CommentService commentService;

    public CommentWebApiServiceImpl(Vertx vertx, CommentService commentService) {
        this.vertx = vertx;
        this.commentService = commentService;
    }

    @Override
    public CommentWebApiService listComments(Integer limit, Long postId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [listComments] | Service Request: {}", request.toJson().encode());
        Optional<Long> postIdOptional = Optional.ofNullable(postId);
        if(postIdOptional.isPresent()) {
            this.commentService.listCommentsByPostId(postIdOptional.get())
                    .subscribe()
                    .with(result -> {
                        ServiceResponse serviceResponse = ServiceResponse.completedWithJson(
                                new JsonArray(
                                        result.stream()
                                                .map(Comment::toJson)
                                                .collect(Collectors.toList())
                                )
                        );
                        resultHandler.handle(Future.succeededFuture(serviceResponse));
                        JsonObject serviceResponseTrace = serviceResponse.toJson();
                        serviceResponseTrace.remove("payload");
                        logger.info("Http Request completed successfully - Operation ID: [listComments] | Service Response: {}", serviceResponseTrace.encode());
                    }, throwable -> {
                        logger.error("Http Request failed - Operation ID: [listComments]. Stacktrace: ", throwable);
                        resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                    });
        } else {
            this.commentService.listComments(limit)
                    .subscribe()
                    .with(result -> {
                        ServiceResponse serviceResponse = ServiceResponse.completedWithJson(
                                new JsonArray(
                                        result.stream()
                                                .map(Comment::toJson)
                                                .collect(Collectors.toList())
                                )
                        );
                        resultHandler.handle(Future.succeededFuture(serviceResponse));
                        JsonObject serviceResponseTrace = serviceResponse.toJson();
                        serviceResponseTrace.remove("payload");
                        logger.info("Http Request completed successfully - Operation ID: [listComments] | Service Response: {}", serviceResponseTrace.encode());
                    }, throwable -> {
                        logger.error("Http Request failed - Operation ID: [listComments]. Stacktrace: ", throwable);
                        resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                    });
        }
        return this;
    }

    @Override
    public CommentWebApiService createComment(Long postId, Comment body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [createComment] | Service Request: {}", request.toJson().encode());
        this.commentService.createComment(postId, body)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse()
                            .setStatusCode(HttpResponseStatus.CREATED.code())
                            .setStatusMessage(HttpResponseStatus.CREATED.reasonPhrase());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [createComment] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [createComment]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }

    @Override
    public CommentWebApiService updateComment(Long postId, Comment body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [updateComment] | Service Request: {}", request.toJson().encode());
        this.commentService.updateComment(postId, body)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .setStatusMessage(HttpResponseStatus.NO_CONTENT.reasonPhrase());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [updateComment] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [updateComment]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }

    @Override
    public CommentWebApiService deleteComment(Long id, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [deleteComment] | Service Request: {}", request.toJson().encode());
        this.commentService.deleteComment(id)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .setStatusMessage(HttpResponseStatus.NO_CONTENT.reasonPhrase());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [deleteComment] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [deleteComment]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }
}

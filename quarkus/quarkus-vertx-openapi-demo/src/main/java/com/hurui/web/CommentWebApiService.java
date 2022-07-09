package com.hurui.web;

import com.hurui.entity.Comment;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

@WebApiServiceGen
public interface CommentWebApiService {

    @Fluent
    CommentWebApiService listComments(Integer limit, Long postId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    @Fluent
    CommentWebApiService createComment(Long postId, Comment body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    @Fluent
    CommentWebApiService updateComment(Long postId, Comment body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    @Fluent
    CommentWebApiService deleteComment(Long id, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

}

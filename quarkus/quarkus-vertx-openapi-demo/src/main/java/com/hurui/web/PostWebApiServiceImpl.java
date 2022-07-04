package com.hurui.web;

import com.hurui.entity.Post;
import com.hurui.service.PostService;
import io.netty.handler.codec.http.HttpResponseStatus;
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
import java.util.stream.Collectors;

@Dependent
@Transactional
public class PostWebApiServiceImpl implements PostWebApiService {

    private static final Logger logger = LoggerFactory.getLogger(PostWebApiServiceImpl.class);

    private final Vertx vertx;
    private final PostService postService;

    public PostWebApiServiceImpl(Vertx vertx, PostService postService) {
        this.vertx = vertx;
        this.postService = postService;
    }

    @Override
    public PostWebApiService listPosts(Integer limit, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [listPosts] | Service Request: {}", request.toJson().encode());
        this.postService.listPosts(limit)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = ServiceResponse.completedWithJson(
                            new JsonArray(
                                    result.stream()
                                            .map(Post::toJson)
                                            .collect(Collectors.toList())
                            )
                    );
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [listPosts] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [listPosts]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }

    @Override
    public PostWebApiService createPost(Post body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [createPost] | Service Request: {}", request.toJson().encode());
        this.postService.createPost(body)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse()
                            .setStatusCode(HttpResponseStatus.CREATED.code())
                            .setStatusMessage(HttpResponseStatus.CREATED.reasonPhrase());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [createPosts] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [createPost]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }

    @Override
    public PostWebApiService updatePost(Post body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [updatePost] | Service Request: {}", request.toJson().encode());
        this.postService.updatePost(body)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .setStatusMessage(HttpResponseStatus.NO_CONTENT.reasonPhrase());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [createPosts] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [createPost]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }

    @Override
    public PostWebApiService deletePost(Long id, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [deletePost] | Service Request: {}", request.toJson().encode());
        this.postService.deletePost(id)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .setStatusMessage(HttpResponseStatus.NO_CONTENT.reasonPhrase());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [deletePost] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [deletePost]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }

    @Override
    public PostWebApiService getPostChangelog(Long id, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        logger.info("Incoming Http Request - Operation ID: [getPostChangelog] | Service Request: {}", request.toJson().encode());
        this.postService.getPostChangelog(id)
                .subscribe()
                .with(result -> {
                    ServiceResponse serviceResponse = new ServiceResponse().setStatusCode(HttpResponseStatus.OK.code());
                    serviceResponse.setStatusMessage(HttpResponseStatus.OK.reasonPhrase());
                    serviceResponse.setPayload(result.toBuffer());
                    resultHandler.handle(Future.succeededFuture(serviceResponse));
                    JsonObject serviceResponseTrace = serviceResponse.toJson();
                    serviceResponseTrace.remove("payload");
                    logger.info("Http Request completed successfully - Operation ID: [getPostChangelog] | Service Response: {}", serviceResponseTrace.encode());
                }, throwable -> {
                    logger.error("Http Request failed - Operation ID: [getPostChangelog]. Stacktrace: ", throwable);
                    resultHandler.handle(Future.failedFuture(new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), throwable)));
                });
        return this;
    }
}

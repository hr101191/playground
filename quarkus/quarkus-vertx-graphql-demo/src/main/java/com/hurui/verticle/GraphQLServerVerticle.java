package com.hurui.verticle;

import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.schema.VertxDataFetcher;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.handler.BodyHandler;
import io.vertx.mutiny.ext.web.handler.StaticHandler;
import io.vertx.mutiny.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.mutiny.ext.web.handler.graphql.GraphiQLHandler;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static java.util.stream.Collectors.toList;

@Dependent
public class GraphQLServerVerticle extends AbstractVerticle {

    private final Boolean enableGraphiQL;
    private final Router router;
    private List<Link> links;

    public GraphQLServerVerticle(@ConfigProperty(name = "application.graphql.ide.enabled") Boolean enableGraphiQL, Router router) {
        this.enableGraphiQL = enableGraphiQL;
        this.router = router;
    }

    public void start(Promise<Void> startPromise) throws Exception {
        prepareData();
        Router vertxRouter = Router.router(this.vertx);
        vertxRouter.route().handler(BodyHandler.create());
        vertxRouter.route("/graphql").handler(GraphQLHandler.create(createGraphQL()));
        GraphiQLHandlerOptions options = new GraphiQLHandlerOptions().setEnabled(this.enableGraphiQL);
        vertxRouter.route("/graphiql/*").handler(GraphiQLHandler.create(options));
        //static handler to access GraphQL IDE css and js
        vertxRouter.route().handler(StaticHandler.create("graphiql"));
        this.router.mountSubRouter("/", vertxRouter);
    }

    private GraphQL createGraphQL() {
        String schema = this.vertx.fileSystem().readFileBlocking("links.graphqls").toString();

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> {
                    VertxDataFetcher<List<Link>> getAllLinks = VertxDataFetcher.create(this::getAllLinks);
                    return builder.dataFetcher("allLinks", getAllLinks);
                })
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        return GraphQL.newGraphQL(graphQLSchema)
                .build();
    }

    private void prepareData() {
        User peter = new User("Peter");
        User paul = new User("Paul");
        User jack = new User("Jack");

        links = new ArrayList<>();
        links.add(new Link("https://vertx.io", "Vert.x project", peter));
        links.add(new Link("https://www.eclipse.org", "Eclipse Foundation", paul));
        links.add(new Link("http://reactivex.io", "ReactiveX libraries", jack));
        links.add(new Link("https://www.graphql-java.com", "GraphQL Java implementation", peter));
    }

    private Future<List<Link>> getAllLinks(DataFetchingEnvironment env) {
        boolean secureOnly = env.getArgument("secureOnly");
        List<Link> result = links.stream()
                .filter(link -> !secureOnly || link.getUrl().startsWith("https://"))
                .collect(toList());
        return Future.succeededFuture(result);
    }
}

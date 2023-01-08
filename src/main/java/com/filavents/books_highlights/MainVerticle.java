package com.filavents.books_highlights;

import com.filavents.books_highlights.configs.Database;
import com.filavents.books_highlights.controllers.NoteController;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import java.util.concurrent.TimeUnit;

public class MainVerticle {

  static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {

    // Init vertx
    VertxOptions vertxOptions = new VertxOptions()
      .setMaxWorkerExecuteTime(10)
      .setMaxWorkerExecuteTimeUnit(TimeUnit.MINUTES);
    var vertx = Vertx.vertx(vertxOptions);

    // Init the EMF
    Database.getEntityManagerFactory();

    // Init HTTP server
    HttpServerOptions serverOptions = new HttpServerOptions();
    serverOptions.setCompressionSupported(true);
    HttpServer server = vertx.createHttpServer(serverOptions);

    // Init Router
    Router router = Router.router(vertx);

    // Middleware code
    router.route().handler(ctx -> {
      ctx.response().putHeader("x-powered-by", "vert.x");
      ctx.response().putHeader("Access-Control-Allow-Origin", "*");
      ctx.next();
    });

    // Routers
    router.get("/api/books/sync").blockingHandler(NoteController::syncBooks);

    // setindexpage return index.html page
    //router.get("/").handler(ctx -> ctx.response().sendFile("web/reddit-ama-web/dist/reddit-ama-web/index.html"));

    // Frontend router path
    //router.get("/view/*").handler(ctx -> ctx.response().sendFile("web/reddit-ama-web/dist/reddit-ama-web/index.html"));

    // Set static web root
    //router.route("/*").handler(StaticHandler.create().setWebRoot("web/reddit-ama-web/dist/reddit-ama-web"));

    // Start the server
    int runningPort = Integer.parseInt(System.getenv("PORT"));
    server.requestHandler(router).listen(runningPort).andThen(httpServerAsyncResult -> {
      if (httpServerAsyncResult.succeeded()) {
        logger.info("Server started on port: " + runningPort);
      } else {
        logger.error(httpServerAsyncResult.cause());
      }
    });
  }
}

package roomservice.verticles;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;

public class HttpVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);
    private final int port;

    public HttpVerticle(final int port) {
        this.port = port;
    }

    @Override
    public void start() {
        final Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.PUT)
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type"));

        router.get("/light")
            .handler( ctx -> this.getData(ctx, null,"request.light"));
        
        router.get("/roll")
            .handler( ctx -> this.getData(ctx, null, "request.rollpercentage"));
        
        router.get("/history/light")
            .handler( ctx -> this.getDataParam(ctx, "light", "size", "request.history"));
        
        router.put("/light")
            .handler( ctx -> this.sendData(ctx, "controller.light", "status"));

        router.put("/roll")
            .handler( ctx -> this.sendData(ctx, "controller.rollpercentage", "percentage"));

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(this.port)
            .onSuccess(server -> {
               this.logger.info("HTTP server started on port {}", server.actualPort());
            })
            .onFailure(event -> {
               this.logger.warn("Failed to start HTTP server: {}", event.getMessage());
            });
    }

    private void getData(final RoutingContext ctx, final String message, final String address) {
        vertx.eventBus().request(address, message, handler -> {
            if (handler.succeeded()) {
                ctx.response().end((String)handler.result().body());
            } else {
                ctx.response().end();
                this.logger.warn("Can not get data for address: {}", address);
            }
        });
    }
    
    private void getDataParam(final RoutingContext ctx, final String message, final String paramName, final String address) {
        final String parameter = ctx.request().getParam(paramName, "");
        vertx.eventBus().request(address, new JsonObject(Map.of("name", message, "parameter", parameter)), handler -> {
            if (handler.succeeded()) {
                ctx.response().end((String)handler.result().body());
            } else {
                ctx.response().end();
                this.logger.warn("Can not get data for address: {}", address);
            }
        });
    }
    
    private void sendData(final RoutingContext ctx, final String address, final String parameter) {
    	ctx.request().bodyHandler(body -> {
    	    final JsonObject jsonObj = new JsonObject(body.toString());
    	    final String param = jsonObj.getString(parameter);
            vertx.eventBus().send(address, param);
            ctx.response().end();
    	});
    }
}

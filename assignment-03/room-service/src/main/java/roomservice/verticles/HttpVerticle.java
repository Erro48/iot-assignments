package roomservice.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;

public class HttpVerticle extends AbstractVerticle {

    private final int port;

    public HttpVerticle(final int port) {
        this.port = port;
    }

    @Override
    public void start() {
        final Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.PUT)
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type"));


        router.get("/light")
            .handler( ctx -> this.getData(ctx, "request.light"));
        
        router.get("/roll")
            .handler( ctx -> this.getData(ctx, "request.rollpercentage"));
        
        router.get("/lighttime")
            .handler( ctx -> this.getData(ctx,  "request.lighttime"));
        
        router.put("/light")
            .handler( ctx -> this.sendData(ctx, "controller.light", "status"));

        router.put("/roll")
            .handler( ctx -> this.sendData(ctx, "controller.rollpercentage", "percentage"));

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(this.port)
            .onSuccess(server -> {
                System.out.println("HTTP server started on port " + server.actualPort());
            })
            .onFailure(event -> {
                System.out.println("Failed to start HTTP server:" + event.getMessage());
            });
    }

    private void getData(final RoutingContext ctx, final String address) {
        vertx.eventBus().request(address, null, handler -> {
            if (handler.succeeded()) {
                ctx.response().end((String)handler.result().body());
            } else {
                ctx.response().end();
                System.err.println("Can not get data for address: " + address);
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

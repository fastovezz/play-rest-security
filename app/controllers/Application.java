package controllers;

import play.Configuration;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * @author Maks Fastovets.
 */
public class Application extends Controller {

    public static final String API_ETADIRECT_HOST = "api.etadirect.host";
    public static final String OSFC_SWAGGER_JSON = "osfc.swagger.json";

    @Inject
    WSClient ws;


    @Inject
    Configuration config;

    public Result index1() {
        return ok(index.render());
    }

    public Result google() {
        return redirect("http://google.com");
    }

    public CompletionStage<Result> proxy(String path) {

        return ws.url(config.getString(API_ETADIRECT_HOST) + request().path())
//                .setHeader(request().getHeader()))
                .get()
                .thenApply(response ->
                        status(response.getStatus(), response.getBody())
                                .withHeader("Access-Control-Allow-Origin", "*")
                                .as(response.getHeader("Content-Type")));
    }

    public CompletionStage<Result> osfcApi() {
        return ws.url(config.getString(OSFC_SWAGGER_JSON))
                .setMethod(request().method())
                .execute() // should be used with #setMethod()
                .thenApply(response ->
                        status(response.getStatus(), response.getBody())
//                                .withHeader("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization")
                                .withHeader("Access-Control-Allow-Origin", "*")
                                .as(response.getHeader("Content-Type")));
    }
}

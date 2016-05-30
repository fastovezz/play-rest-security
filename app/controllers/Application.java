package controllers;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * @author Maks Fastovets.
 */
public class Application extends Controller {

    public static final String HTTPS_API_ETADIRECT_COM = "https://api.etadirect.com";
    @Inject
    WSClient ws;

    public Result index1() {
        return ok(index.render());
    }

    public Result google() {
        return redirect("http://google.com");
    }

    public CompletionStage<Result> proxy(String path) {

        return ws.url(HTTPS_API_ETADIRECT_COM + request().path())
//                .setHeader(request().getHeader()))
                .get()
                .thenApply(response ->
                        status(response.getStatus(), response.getBody())
                                .as(response.getHeader("Content-Type")));
    }
}

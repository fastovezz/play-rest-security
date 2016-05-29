package controllers;

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

    @Inject
    WSClient ws;

    public Result index1() {
        return ok(index.render());
    }

    public Result google() {
        return redirect("http://google.com");
    }
}

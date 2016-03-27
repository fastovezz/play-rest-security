package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * @author Maks Fastovets.
 */
public class Application  extends Controller {

    public Result index1() {
        return ok(index.render());
    }
}

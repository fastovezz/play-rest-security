package controllers;

import play.Configuration;
import play.data.FormFactory;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * @author Maks Fastovets.
 */

@Security.Authenticated(Secured.class)
public class MoviesController extends Controller {
    private static final String API_KEY = "themoviedb.api_key";
    private static final String SEARCH_MOVIE_URL = "https://api.themoviedb.org/3/search/movie";
    @Inject
    FormFactory formFactory;

    @Inject
    Configuration config;

    @Inject
    WSClient ws;

    public CompletionStage<Result> getMovies() {
        String searchText = formFactory.form().bindFromRequest().get("searchText");

        return ws.url(SEARCH_MOVIE_URL)
                .setQueryParameter("api_key", config.getString(API_KEY))
                .setQueryParameter("query", searchText)
                .get()
                .thenApply(response -> ok(response.asJson()));
    }

}

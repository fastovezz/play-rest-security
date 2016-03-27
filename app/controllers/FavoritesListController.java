package controllers;

import models.FavoritesList;
import models.Movie;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

import static play.libs.Json.toJson;

/**
 * @author Maks Fastovets.
 */
@Security.Authenticated(Secured.class)
public class FavoritesListController extends Controller {
    @Inject
    FormFactory formFactory;

    public Result getAllFavoritesLists() {
        return ok(toJson(FavoritesList.findByUser(SecurityController.getUser())));
    }


    public Result deleteFavoritesList(Long id) {
        FavoritesList favoritesList = FavoritesList.findById(id);
        String deletedId = null;
        if (favoritesList != null) {
            favoritesList.moviesList.clear();
            favoritesList.save();
            favoritesList.delete();
            deletedId = String.valueOf(id);
        }
        return ok(deletedId);
    }

    public Result addMovieToFavList() {
        Form<Movie> form = formFactory.form(Movie.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        } else {
            String favListIdAsString = formFactory.form().bindFromRequest().get("favListId");
            if (favListIdAsString != null) {

                Movie movie = form.get();
                Movie movieFromDb = Movie.findByThemoviedbId(movie.themoviedbId);
                if(movieFromDb == null) {
                    movie.save();
                } else {
                    movie = movieFromDb;
                }
                FavoritesList favoritesList =
                        FavoritesList.findByUserAndId(SecurityController.getUser(), Long.valueOf(favListIdAsString));
                if(!favoritesList.moviesList.contains(movie)) {
                    movie.favoritesList.add(favoritesList);
                    favoritesList.moviesList.add(movie);
                    favoritesList.save();
                }
                return ok(toJson(movie));
            } else {
                return badRequest("favListId is not provided");
            }
        }
    }

    public Result createFavoritesList() {
        Form<FavoritesList> form = formFactory.form(FavoritesList.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        } else {
            FavoritesList favoritesList = form.get();
            favoritesList.user = SecurityController.getUser();
            favoritesList.save();
            return ok(toJson(favoritesList));
        }
    }


    public Result getMoviesByListId(Long id) {
        FavoritesList favoritesList = FavoritesList.findById(id);

        return ok(toJson(favoritesList.moviesList));
    }

    public Result deleteMovieFromFavoritesList(Long favListId, Long movieId) {
        FavoritesList favoritesList = FavoritesList.findById(favListId);
        if(favoritesList != null) {
            Movie movie = Movie.findById(movieId);
            if(movie != null && favoritesList.moviesList.contains(movie)) {
                favoritesList.moviesList.remove(movie);
                favoritesList.save();
                return ok("movie deleted from list successfully");
            }
        }
        return ok("didn't find movie to delete or list to delete from");
    }


}

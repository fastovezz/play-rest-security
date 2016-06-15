package controllers;

import io.swagger.annotations.*;
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
@Api(value = "/favlists", description = "Operations about FavLists")
@Security.Authenticated(Secured.class)
public class FavoritesListController extends Controller {
    @Inject
    FormFactory formFactory;

    @ApiOperation(nickname = "getAllFavoritesLists", value = "Get all FavLists",
            notes = "Returns all FavLists", response = FavoritesList.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Result getAllFavoritesLists() {
        return ok(toJson(FavoritesList.findByUser(SecurityController.getUser())));
    }


    public Result deleteFavoritesList(Long id) {
        FavoritesList favoritesList = FavoritesList.findById(id);
        String deletedId = null;
        if (favoritesList != null) {
            favoritesList.getMoviesList().clear();
            favoritesList.save();
            favoritesList.delete();
            deletedId = String.valueOf(id);
        }
        return ok(deletedId);
    }

    @ApiOperation(
            nickname = "addMovieToFavList",
            value = "Add movie to favList",
            notes = "Add movie to favList",
            httpMethod = "POST",
            response = Movie.class
    )
    @ApiParam(
            name = "favListId",
            value = "favList id"
    )
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            name = "body",
                            dataType = "models.Movie",
                            required = true,
                            paramType = "body",
                            value = "models.Movie"
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Json Processing Exception")
            }
    )
    public Result addMovieToFavList(Long id) {
        Form<Movie> form = formFactory.form(Movie.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        } else {
            Movie movie = form.get();
            Movie movieFromDb = Movie.findByThemoviedbId(movie.getThemoviedbId());
            if (movieFromDb == null) {
                movie.save();
            } else {
                movie = movieFromDb;
            }
            FavoritesList favoritesList =
                    FavoritesList.findByUserAndId(SecurityController.getUser(), id);
            if (!favoritesList.getMoviesList().contains(movie)) {
                movie.getFavoritesList().add(favoritesList);
                favoritesList.getMoviesList().add(movie);
                favoritesList.save();
            }
            return ok(toJson(movie));
        }
    }

    @ApiOperation(
            nickname = "createFavoritesList",
            value = "Create Favorites List",
            notes = "Create Favorites List record",
            httpMethod = "POST",
            response = FavoritesList.class
    )
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            name = "body",
                            dataType = "models.FavoritesList",
                            required = true,
                            paramType = "body",
                            value = "models.FavoritesList"
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Json Processing Exception")
            }
    )
    public Result createFavoritesList() {
        Form<FavoritesList> form = formFactory.form(FavoritesList.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        } else {
            FavoritesList favoritesList = form.get();
            favoritesList.setUser(SecurityController.getUser());
            favoritesList.save();
            return ok(toJson(favoritesList));
        }
    }


    @ApiOperation(nickname = "getMoviesByListId", value = "Get all movies of certain favlist",
            notes = "Returns all FavLists", response = FavoritesList.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "favlist not found")
    })
    public Result getMoviesByListId(Long id) {
        FavoritesList favoritesList = FavoritesList.findById(id);

        if (favoritesList == null) {
            return notFound();
        } else {
            return ok(toJson(favoritesList.getMoviesList()));
        }
    }

    public Result deleteMovieFromFavoritesList(Long favListId, Long movieId) {
        FavoritesList favoritesList = FavoritesList.findById(favListId);
        if (favoritesList != null) {
            Movie movie = Movie.findById(movieId);
            if (movie != null && favoritesList.getMoviesList().contains(movie)) {
                favoritesList.getMoviesList().remove(movie);
                favoritesList.save();
                return ok("movie deleted from list successfully");
            }
        }
        return ok("didn't find movie to delete or list to delete from");
    }


}

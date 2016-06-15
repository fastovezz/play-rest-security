package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * @author Maks Fastovets.
 */
@Entity
public class Movie extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1024)
    private String title;

    @Column
    private String thumbnailUrl;

    @Column(unique = true, nullable = false)
    @Constraints.Required
    private Long themoviedbId;

    @ManyToMany(mappedBy="moviesList")
    @JsonIgnore
    private List<FavoritesList> favoritesList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return themoviedbId.equals(movie.themoviedbId);

    }

    @Override
    public int hashCode() {
        return themoviedbId.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getThemoviedbId() {
        return themoviedbId;
    }

    public void setThemoviedbId(Long themoviedbId) {
        this.themoviedbId = themoviedbId;
    }

    public List<FavoritesList> getFavoritesList() {
        return favoritesList;
    }

    public void setFavoritesList(List<FavoritesList> favoritesList) {
        this.favoritesList = favoritesList;
    }

    public static Movie findByThemoviedbId(Long themopviedbId) {
        Model.Finder<Long, Movie> finder = new Model.Finder<>(Movie.class);
        return finder.where().eq("themoviedbId", themopviedbId).findUnique();
    }

    public static Movie findById(Long id) {
        Model.Finder<Long, Movie> finder = new Model.Finder<>(Movie.class);
        return finder.where().eq("id", id).findUnique();
    }
}

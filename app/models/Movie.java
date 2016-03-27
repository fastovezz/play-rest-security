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
    public Long id;

    @Column(nullable = false, length = 1024)
    public String title;

    @Column
    public String thumbnailUrl;

    @Column(unique = true, nullable = false)
    @Constraints.Required
    public Long themoviedbId;

    @ManyToMany(mappedBy="moviesList")
    @JsonIgnore
    public List<FavoritesList> favoritesList;

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

    public static Movie findByThemoviedbId(Long themopviedbId) {
        Model.Finder<Long, Movie> finder = new Model.Finder<>(Movie.class);
        return finder.where().eq("themoviedbId", themopviedbId).findUnique();
    }

    public static Movie findById(Long id) {
        Model.Finder<Long, Movie> finder = new Model.Finder<>(Movie.class);
        return finder.where().eq("id", id).findUnique();
    }
}

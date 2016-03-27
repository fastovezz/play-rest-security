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
public class FavoritesList extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 1024, nullable = false, unique = true)
    @Constraints.MaxLength(1024)
    @Constraints.Required
    public String name;

    @ManyToOne
    @JsonIgnore
    public User user;

    @ManyToMany
    @JoinTable(name = "MOVIE_LIST",
            joinColumns =
                    {
                            @JoinColumn(name = "LIST_ID")
                    },
            inverseJoinColumns =
                    {
                            @JoinColumn(name = "MOVIE_ID")
                    })
    @JsonIgnore
    public List<Movie> moviesList;

    public FavoritesList(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public static List<FavoritesList> findByUser(User user) {
        Model.Finder<Long, FavoritesList> finder = new Model.Finder<>(FavoritesList.class);
        return finder.where().eq("user", user).findList();
    }

    public static FavoritesList findByUserAndId(User user, Long id) {
        Model.Finder<Long, FavoritesList> finder = new Model.Finder<>(FavoritesList.class);
        return finder.where().eq("user", user).eq("id", id).findUnique();
    }

    public static FavoritesList findById(Long id) {
        Model.Finder<Long, FavoritesList> finder = new Model.Finder<>(FavoritesList.class);
        return finder.where().eq("id", id).findUnique();
    }
}

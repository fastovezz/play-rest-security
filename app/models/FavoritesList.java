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
    private Long id;

    @Column(length = 1024, nullable = false, unique = true)
    @Constraints.MaxLength(1024)
    @Constraints.Required
    private String name;

    @ManyToOne
    @JsonIgnore
    private User user;

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
    private List<Movie> moviesList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

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

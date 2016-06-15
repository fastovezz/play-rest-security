package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authToken;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress.toLowerCase();
    }

    @Column(length = 64, nullable = false)
    private byte[] shaPassword;

    @Transient
    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(256)
    @JsonIgnore
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        shaPassword = getSha512(password);
    }

    @Column(length = 256, nullable = false)
    @Constraints.Required
    @Constraints.MinLength(2)
    @Constraints.MaxLength(256)
    private String fullName;

    @Column(nullable = false)
    private Date creationDate;

    public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }

    public User() {
        this.creationDate = new Date();
    }

    public User(String emailAddress, String password, String fullName) {
        setEmailAddress(emailAddress);
        setPassword(password);
        this.fullName = fullName;
        this.creationDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public byte[] getShaPassword() {
        return shaPassword;
    }

    public void setShaPassword(byte[] shaPassword) {
        this.shaPassword = shaPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Finder<Long, User> find = new Finder<>(User.class);

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try {
            return find.where().eq("authToken", authToken).findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findByEmailAddressAndPassword(String emailAddress, String password) {
        // todo: verify this query is correct.  Does it need an "and" statement?
        return find.where().eq("emailAddress", emailAddress.toLowerCase()).eq("shaPassword", getSha512(password)).findUnique();
    }

}

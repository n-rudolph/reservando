package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends Model {
    @Id
    @NotNull
    private long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String address;

    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;

    private Photo photo;

    public static Finder<Long, User> find = new Finder<Long,User>(User.class);

    public User() {
    }

    public User(String firstName, String lastName, String address, String email, String password, Photo photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getlastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Photo getPhoto() {
        return photo;
    }

    public User setPhoto(Photo photo) {
        this.photo = photo;
        return this;
    }

    public static List<User> all() {
        return find.all();
    }

    public static boolean isEmailInUse(String email) {
        return find.where().eq("email", email).findRowCount() != 0;
    }

    public static User getUserByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static User findById(long id) {
        return find.byId(id);
    }
}

package models;

import com.avaje.ebean.Model;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;

    @OneToOne
    @Nullable
    private Photo photo;
    private String photoPath;

    public boolean active;

    public static Finder<Long, User> find = new Finder<Long,User>(User.class);

    public User() {
    }

    public User(String firstName, String lastName, Address address, String email, String password, Photo photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.active = true;
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

    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
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

    public String getPhotoPath(){return photoPath;}

    public User setPhoto(Photo photo) {
        this.photo = photo;
        return this;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
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

    public void setActive() {
        active = false;
    }

    public boolean isActive(){
        return active;
    }
}

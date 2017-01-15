package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends Model {
    @Id
    @NotNull
    private long id;
    @NotNull
    private String name;
    @NotNull
    private DateTime birthday;
    @NotNull
    private Address address;

    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;

    private Photo photo;

    public static Finder<Long, User> find = new Finder<Long,User>(User.class);

    public User() {
    }

    public User(long id, String name, DateTime birthday, Address address, String email, String password, Photo photo) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
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

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public User setBirthday(DateTime birthday) {
        this.birthday = birthday;
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

    public User setPhoto(Photo photo) {
        this.photo = photo;
        return this;
    }
}

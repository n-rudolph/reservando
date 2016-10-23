package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class User extends Model {
    @Id
    private long id;
    private String firstname;
    private String lastname;
    private DateTime birthday;
    private String address;
    @Column(unique = true)
    private String email;
    private String password;
    private String gender;
    @Lob
    private byte[] profilePicture;

    public static Finder<Long, User> find = new Finder<Long,User>(User.class);

    public User() {
    }

    public User(long id, String firstname, String lastname, DateTime birthday, String address, String email, String password, String gender, byte[] profilePicture) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.profilePicture = profilePicture;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public User setBirthday(DateTime birthday) {
        this.birthday = birthday;
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

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public User setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }
}

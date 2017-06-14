package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
public class Photo extends Model{

    @Id
    private long id;
    private String name;
    private String path;

    public Photo(@NotNull String name, @NotNull String path){
        this.name = name;
        this.path = path;
    }

    public Photo(@NotNull long id, @NotNull String name, @NotNull String path){
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public Photo setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Photo setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Photo setPath(String path) {
        this.path = path;
        return this;
    }

    public static Finder<Long, Photo> finder = new Finder<>(Photo.class);
}

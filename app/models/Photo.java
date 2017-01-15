package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by rudy on 07/01/17.
 */
public class Photo extends Model{

    @Id
    private long id;

    private String path;

    public Photo(@NotNull long id, @NotNull String path){
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public Photo setId(long id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Photo setPath(String path) {
        this.path = path;
        return this;
    }
}

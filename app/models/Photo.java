package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class Photo extends Model{

    @Id
    private long id;
    private String name;
    @Lob
    private String src;

    public Photo(@NotNull String name, @NotNull String src){
        this.name = name;
        this.src = src;
    }

    public Photo(@NotNull long id, @NotNull String name, @NotNull String src){
        this.id = id;
        this.name = name;
        this.src = src;
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

    public String getSrc() {
        return src;
    }

    public Photo setSrc(String src) {
        this.src = src;
        return this;
    }
}

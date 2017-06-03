package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Cuisine extends Model{
    @Id
    private long id;
    private String name;

    private static Finder<Long, Cuisine> finder = new Finder<Long,Cuisine>(Cuisine.class);

    public Cuisine(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Cuisine() {
    }

    public long getId() {
        return id;
    }

    public Cuisine setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Cuisine setName(String name) {
        this.name = name;
        return this;
    }

    public static Cuisine getCuisine(String cuisineName){return finder.where().eq("name", cuisineName).findUnique();}

    public static Cuisine byId(long id) {
        return finder.byId(id);
    }

    public static List<Cuisine> all() {
        return finder.all();
    }
}

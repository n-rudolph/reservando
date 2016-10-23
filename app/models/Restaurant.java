package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Restaurant extends Model{

    @Id
    private long id;
    private String name;
    private String description;
    private List<DateTime> openingHours;
    private List<Days> openingDays;
    @ManyToMany
    private List<Cuisine> cuisines;

    public static Finder<Long, Restaurant> find = new Finder<Long,Restaurant>(Restaurant.class);

    public Restaurant() {
    }

    public Restaurant(long id, String name, String description, List<DateTime> openingHours, List<Days> openingDays, List<Cuisine> cuisines) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.openingHours = openingHours;
        this.openingDays = openingDays;
        this.cuisines = cuisines;
    }

    public long getId() {
        return id;
    }

    public Restaurant setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Restaurant setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Restaurant setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<DateTime> getOpeningHours() {
        return openingHours;
    }

    public Restaurant setOpeningHours(List<DateTime> openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    public List<Days> getOpeningDays() {
        return openingDays;
    }

    public Restaurant setOpeningDays(List<Days> openingDays) {
        this.openingDays = openingDays;
        return this;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public Restaurant setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
        return this;
    }
}

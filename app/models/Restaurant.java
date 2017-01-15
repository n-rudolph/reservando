package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Restaurant extends Model{

    @Id
    @NotNull
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private DateTime openingHour;
    private DateTime closingHour;
    private List<Days> openingDays;
    @ManyToMany
    private List<Cuisine> cuisines;
    @OneToMany
    private Address address;


    public static Finder<Long, Restaurant> find = new Finder<Long,Restaurant>(Restaurant.class);

    public Restaurant() {
    }

    public Restaurant(long id, String name, String description, Address address, DateTime openingHour, DateTime closingHour, List<Days> openingDays, List<Cuisine> cuisines) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
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

    public DateTime getOpeningHour() {
        return openingHour;
    }

    public Restaurant setOpeningHour(DateTime openingHour) {
        this.openingHour = openingHour;
        return this;
    }

    public DateTime getClosingHour() {
        return closingHour;
    }

    public Restaurant setClosingHour(DateTime closingHour) {
        this.closingHour = closingHour;
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

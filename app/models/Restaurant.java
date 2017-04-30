package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Restaurant extends Model{

    @Id
    @NotNull
    private long id;
    @NotNull
    private String name;
    private String description;
    private String openingHour;
    private String closingHour;
    @ManyToMany
    private List<Day> openingDays;
    @ManyToMany
    private List<Cuisine> cuisines;
    @NotNull
    private String address;
    private boolean published;
    private boolean isLocal;
    @ManyToOne
    private Owner owner;


    public static Finder<Long, Restaurant> find = new Finder<Long,Restaurant>(Restaurant.class);

    public Restaurant() {
        published = false;
    }

    public Restaurant (String name, String address, boolean isLocal){
        this.name = name;
        this.address = address;
        published = false;
        this.isLocal = isLocal;
        openingDays = new ArrayList<>();
        cuisines = new ArrayList<>();
    }
    public Restaurant(String name, String description, String address,
                      String openingHour, String closingHour, List<Day> openingDays,
                      List<Cuisine> cuisines, boolean isLocal, Owner owner) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.openingDays = openingDays;
        this.cuisines = cuisines;
        published = false;
        this.isLocal = isLocal;
        this.owner = owner;
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
        return description == null ? "" : description;
    }

    public Restaurant setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOpeningHour() {
        return openingHour == null ? "" : openingHour;
    }

    public Restaurant setOpeningHour(String  openingHour) {
        this.openingHour = openingHour;
        return this;
    }

    public String getClosingHour() {
        return closingHour == null ? "" : closingHour;
    }

    public Restaurant setClosingHour(String closingHour) {
        this.closingHour = closingHour;
        return this;
    }

    public List<Day> getOpeningDays() {
        return openingDays == null ? Collections.emptyList() : openingDays;
    }

    public Restaurant setOpeningDays(List<Day> openingDays) {
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

    public String getAddress() {
        return address;
    }

    public Restaurant setAddress(String address) {
        this.address = address;
        return this;
    }

    public boolean isPublished() {
        return published;
    }

    public Restaurant setPublished(boolean published) {
        this.published = published;
        return this;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public Restaurant setLocal(boolean local) {
        isLocal = local;
        return this;
    }
}

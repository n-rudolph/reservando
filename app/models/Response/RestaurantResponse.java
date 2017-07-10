package models.Response;

import models.*;

import java.util.List;

public class RestaurantResponse {
    public long id;
    public String name;
    public String description;
    public String openingHour;
    public String closingHour;
    public List<Day> openingDays;
    public List<Cuisine> cuisines;
    public Address address;
    public boolean published;
    public boolean local;
    public boolean isDeleted;
    public List<Meal> menu;
    public Photo photo;
    public double qualification;

    public RestaurantResponse(Restaurant r, double qualification) {
        this.qualification = qualification;
        this.id = r.getId();
        this.name = r.getName();
        this.description = r.getDescription();
        this.openingHour = r.getOpeningHour();
        this.closingHour = r.getClosingHour();
        this.openingDays = r.getOpeningDays();
        this.cuisines = r.getCuisines();
        this.address = r.getAddress();
        this.published = r.isPublished();
        this.local = r.isLocal();
        this.isDeleted = r.isDeleted();
        this.menu = r.getMenu();
        this.photo = r.getPhoto();
    }
}

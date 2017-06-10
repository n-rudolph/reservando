package models.Response;

import models.*;

import java.util.List;

public class LocalResponse {
    public long id;
    public String name;
    public String description;
    public String openingHour;
    public String closingHour;
    public List<Day> openingDays;
    public List<Cuisine> cuisines;
    public String address;
    public boolean published;
    public boolean isLocal;
    public Photo photo;
    public int capacity;

    public LocalResponse(Local local){
        this.id = local.getId();
        this.name = local.getName();
        this.description = local.getDescription();
        this.openingHour = local.getOpeningHour();
        this.closingHour = local.getClosingHour();
        this.openingDays = local.getOpeningDays();
        this.cuisines = local.getCuisines();
        this.address = local.getAddress();
        this.published = local.isPublished();
        this.isLocal = local.isLocal();
        this.photo = local.getPhoto();
        this.capacity = local.getCapacity();
    }
}

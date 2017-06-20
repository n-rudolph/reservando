package models.Response;

import models.*;

import java.util.List;

public class DeliveryResponse {

    public long id;
    public String name;
    public String description;
    public String openingHour;
    public String closingHour;
    public List<Day> openingDays;
    public List<Cuisine> cuisines;
    public Address address;
    public boolean published;
    public boolean isLocal;
    public Photo photo;
    public double radius;
    public int responseTime;

    public DeliveryResponse(Delivery delivery){
        this.id = delivery.getId();
        this.name = delivery.getName();
        this.description = delivery.getDescription();
        this.openingHour = delivery.getOpeningHour();
        this.closingHour = delivery.getClosingHour();
        this.openingDays = delivery.getOpeningDays();
        this.cuisines = delivery.getCuisines();
        this.address = delivery.getAddress();
        this.published = delivery.isPublished();
        this.isLocal = delivery.isLocal();
        this.photo = delivery.getPhoto();
        this.radius = delivery.getRadius();
        this.responseTime = delivery.getResponseTime();
    }
}

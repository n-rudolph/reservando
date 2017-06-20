package models.requestObjects;

import models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantObject {
    public String name;
    public AddressObject address;
    public String description;
    public boolean isLocal;
    public int capacity;
    public double radius;
    public List<Day> days;
    public String startTime;
    public String endTime;
    public List<Cuisine> cuisines;
    public PhotoObject photo;
    public int responseTime;
    public int minsBetweenTurns;

    public Delivery toDelivery(Owner owner){
        return new Delivery(
                name,
                description,
                address.toAddress(),
                startTime,
                endTime,
                days,
                cuisines,
                radius,
                Collections.emptyList(),
                owner,
                responseTime);
    }

    public Local toLocal(Owner owner){
        return new Local(
                name,
                description,
                address.toAddress(),
                startTime,
                endTime,
                days,
                cuisines,
                capacity,
                Collections.emptyList(),
                owner,
                minsBetweenTurns);
    }
}

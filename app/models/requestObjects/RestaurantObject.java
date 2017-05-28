package models.requestObjects;

import models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantObject {
    public String name;
    public String address;
    public String description;
    public boolean isLocal;
    public int capacity;
    public double radius;
    public List<String> days;
    public String startTime;
    public String endTime;
    public List<Cuisine> cuisines;

    public Delivery toDelivery(Owner owner){
        return new Delivery(
                name,
                description,
                address,
                startTime,
                endTime,
                toDays(days),
                cuisines,
                radius,
                Collections.emptyList(),
                owner);
    }

    public Local toLocal(Owner owner){
        return new Local(
                name,
                description,
                address,
                startTime,
                endTime,
                toDays(days),
                cuisines,
                capacity,
                Collections.emptyList(),
                owner);
    }

    private List<Day> toDays(List<String> days){
        final List<Day> result = new ArrayList<>();
        for (String day : days) {
            result.add(Day.valueOf(day));
        }
        return result;
    }

}

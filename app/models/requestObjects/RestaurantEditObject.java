package models.requestObjects;

import models.Cuisine;
import models.Day;

import java.util.List;

public class RestaurantEditObject {
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
    public int responseTime;
    public int minsBetweenTurns;
}

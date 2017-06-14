package models.requestObjects;


import models.Meal;

public class MealObject {
    public long id;
    public String name;
    public String description;
    public double price;
    public PhotoObject photo;

    public Meal toMeal() {
        return new Meal(name, description, price);
    }
}

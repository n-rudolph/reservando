package models.requestObjects;


import models.Meal;

public class MealObject {
    public String name;
    public String description;
    public double price;

    public Meal toMeal() {
        return new Meal(name, description, price, null);
    }
}

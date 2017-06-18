package models.requestObjects;

import models.Meal;
import models.MealOrder;

public class MealOrderObject {
    public Meal meal;
    public int amount;

    public MealOrder toMealOrder(){
        return new MealOrder(meal.getId(), amount);
    }
}

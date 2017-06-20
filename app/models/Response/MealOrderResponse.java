package models.Response;

import models.Meal;
import models.MealOrder;

public class MealOrderResponse {
    public Long id;
    public Meal meal;
    public int amount;

    public MealOrderResponse(MealOrder mealOrder){
        id = mealOrder.getId();
        meal = Meal.byId(mealOrder.getMealId());
        amount = mealOrder.getAmount();
    }
}

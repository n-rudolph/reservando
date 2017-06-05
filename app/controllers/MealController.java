package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Meal;
import models.Restaurant;
import models.requestObjects.MealObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class MealController extends Controller {

    public Result newMeal(String rid){
        final JsonNode jsonNode = request().body().asJson();
        final MealObject mealObject = Json.fromJson(jsonNode, MealObject.class);

        final long restaurantId = Long.parseLong(rid);
        final Restaurant restaurant = Restaurant.byId(restaurantId);

        restaurant.addMeal(mealObject.toMeal());
        restaurant.update();

        return ok("");
    }

    public Result delete(String mId){
        final long id = Long.parseLong(mId);
        final Meal meal = Meal.byId(id);
        meal.setDeleted(true);
        meal.update();
        return ok("Meal deleted successfully");
    }

    public Result update(String mId){
        //TODO: implement
        return ok("");
    }
}

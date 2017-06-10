package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Meal;
import models.Photo;
import models.Restaurant;
import models.requestObjects.MealObject;
import models.requestObjects.PhotoObject;
import modules.ImageUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.stream.Collectors;

public class MealController extends Controller {

    public Result getMenu(String rid){
        return ok(Json.toJson(Restaurant.byId(Long.parseLong(rid)).getMenu().stream().filter(m -> !m.isDeleted()).collect(Collectors.toList())));
    }

    public Result newMeal(String rid){
        final JsonNode jsonNode = request().body().asJson();
        final MealObject mealObject = Json.fromJson(jsonNode, MealObject.class);

        final long restaurantId = Long.parseLong(rid);
        final Restaurant restaurant = Restaurant.byId(restaurantId);


        final Photo photo = saveImage(mealObject.photo);
        if (photo == null){
            return badRequest();
        }
        final Meal meal = mealObject.toMeal();
        meal.save();
        photo.save();
        meal.setImage(photo);
        meal.setRestaurant(restaurant);
        meal.update();

        restaurant.addMeal(meal);
        restaurant.update();

        return ok(Json.toJson(meal));
    }

    public Result delete(String mid){
        final long id = Long.parseLong(mid);
        final Meal meal = Meal.byId(id);
        meal.setDeleted(true);
        meal.update();
        return ok("Meal deleted successfully");
    }

    public Result update(String mid){
        //TODO: implement
        return ok("");
    }

    private Photo saveImage(PhotoObject photo){
        final List<String> photoInfo = ImageUtils.saveImage(photo.src, photo.name);
        if (photoInfo == null)
            return null;
        return new Photo(photoInfo.get(0), photoInfo.get(1));
    }
}

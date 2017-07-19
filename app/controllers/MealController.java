package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Meal;
import models.Photo;
import models.Response.MealResponse;
import models.Restaurant;
import models.requestObjects.MealEditObject;
import models.requestObjects.MealObject;
import models.requestObjects.PhotoObject;
import modules.ImageUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.*;

import java.util.List;
import java.util.stream.Collectors;

public class MealController extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public MealController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result getMenu(String rid){
        final List<MealResponse> menu = Meal.getByRestaurant(Restaurant.byId(Long.parseLong(rid))).stream().filter(m -> !m.isDeleted()).map(MealResponse::new).collect(Collectors.toList());
        return ok(Json.toJson(menu));
    }

    public Result newMeal(String rid){
        final JsonNode jsonNode = request().body().asJson();
        final MealObject mealObject = Json.fromJson(jsonNode, MealObject.class);

        final long restaurantId = Long.parseLong(rid);
        final Restaurant restaurant = Restaurant.byId(restaurantId);


        final Photo photo = ImageUtils.saveImage(mealObject.photo);
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

        return ok(Json.toJson(new MealResponse(meal)));
    }

    public Result delete(String mid){
        //I18N
        Messages messages =  messagesApi.preferred(request());

        final long id = Long.parseLong(mid);
        final Meal meal = Meal.byId(id);
        meal.setDeleted(true);
        meal.update();
        String mealDeleted = messages.at("meal.server.response.delete.successfully", "");
        return ok(mealDeleted);
        //return ok("Meal deleted successfully");
    }

    public Result update(String mid){
        final JsonNode jsonNode = request().body().asJson();
        final MealEditObject mealEditObject = Json.fromJson(jsonNode, MealEditObject.class);

        final Meal meal = Meal.byId(Long.parseLong(mid));
        meal.setName(mealEditObject.name).setDescription(mealEditObject.description).setPrice(mealEditObject.price);

        if (mealEditObject.photo != null){
            final PhotoObject newPhoto = mealEditObject.photo;
            final Photo photo = ImageUtils.saveImage(newPhoto);
            if (photo == null){
                return badRequest();
            }
            if (meal.getImage() != null) {
                final Photo oldPhoto = Photo.finder.byId(meal.getImage().getId());
                if (!ImageUtils.deleteImage("./public/images/imgApp/" + oldPhoto.getName())) {
                    return badRequest();
                }
            }
            photo.save();
            meal.setImage(photo);
        }
        meal.update();
        return ok(Json.toJson(new MealResponse(meal)));
    }
}

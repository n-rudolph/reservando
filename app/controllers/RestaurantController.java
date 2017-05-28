package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Response.RedirectResponse;
import models.Response.RestaurantsResponse;
import models.requestObjects.RestaurantObject;
import modules.Utils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;

public class RestaurantController extends Controller {

    public Result viewOwnerRestaurants(){
        return ok();
    }

    public Result addRestaurant(){
        final JsonNode jsonNode = request().body().asJson();
        final RestaurantObject restaurantObject = Json.fromJson(jsonNode, RestaurantObject.class);
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);
        if (restaurantObject.isLocal){
            final Local local = restaurantObject.toLocal(owner);
            local.save();
            owner.addRestaurant(local);
            owner.update();
            return ok(Json.toJson(local));
        } else {
            final Delivery delivery = restaurantObject.toDelivery(owner);
            delivery.save();
            owner.addRestaurant(delivery);
            owner.update();
            return ok(Json.toJson(delivery));
        }
    }

    public Result getAll(){
        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        return ok(Json.toJson(ownerbyEmail.getRestaurants()));
    }

    public Result getFirsts(){
        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        final List<Restaurant> restaurants = ownerbyEmail.getRestaurants();
        int max = restaurants.size() < 5 ? restaurants.size() : 5;

        final RestaurantsResponse response = new RestaurantsResponse(200, "ok", restaurants.subList(0, max), restaurants.size() > 5);

        return ok(Json.toJson(response));
    }

    public Result changeState(){
        final JsonNode body = request().body().asJson();
        final long id = body.get("id").asLong();
        final boolean state = body.get("state").asBoolean();
        final Restaurant restaurant = Restaurant.byId(id);
        restaurant.setPublished(state).update();
        return ok("State modified successfully");
    }

    public Result openRestaurantProfile(){
        final JsonNode jsonNode = request().body().asJson();
        final JsonNode restaurantId = jsonNode.path("id");
        String redirectUrl;
        if (jsonNode.path("local").asBoolean())
            redirectUrl = "/local/profile";
        else redirectUrl = "/delivery/profile";
        redirectUrl += "?rid="+restaurantId;
        final RedirectResponse response = new RedirectResponse(200, "ok", redirectUrl);
        return ok(Json.toJson(response));
    }

    public Result addOpenDay(){
        /*final JsonNode body = request().body().asJson();
        if (body.path("id").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Restaurant id missing", null));
        final Restaurant restaurant = Restaurant.byId(body.path("id").asLong());
        if (body.path("dayId").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Day missing", null));
        final Day day = Day.byId(body.path("dayId").asLong());
        restaurant.addDay(day);*/
        return ok(Utils.generateResponse("200", "Day added correctly", null));
    }

    public Result removeOpenDay(){
        /*final JsonNode body = request().body().asJson();
        if (body.path("id").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Restaurant id missing", null));
        final Restaurant restaurant = Restaurant.byId(body.path("id").asLong());
        if (body.path("dayId").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Day missing", null));
        final Day day = Day.byId(body.path("dayId").asLong());
        restaurant.removeDay(day);*/
        return ok(Utils.generateResponse("200", "Day removed correctly", null));
    }

    public Result addCuisine(){
        final JsonNode body = request().body().asJson();
        if (body.path("id").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Restaurant id missing", null));
        final Restaurant restaurant = Restaurant.byId(body.path("id").asLong());
        Cuisine cuisine;
        if (body.path("dayId").isMissingNode()) {
            cuisine = new Cuisine();
            cuisine.setName(body.path("newCuisine").asText().trim());
            cuisine.save();
            cuisine= Cuisine.getCuisine(body.path("newCuisine").asText().trim());
        }else {
            cuisine = Cuisine.byId(body.path("cuisineId").asLong());
        }
        restaurant.addCuisine(cuisine);
        return ok(Utils.generateResponse("200", "Cuisine added correctly", null));
    }

    public Result removeCuisine(){
        final JsonNode body = request().body().asJson();
        if (body.path("id").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Restaurant id missing", null));
        final Restaurant restaurant = Restaurant.byId(body.path("id").asLong());
        if (body.path("cuisineId").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Cuisine missing", null));
        final Cuisine cuisine = Cuisine.byId(body.path("cuisineId").asLong());
        restaurant.removeCuisine(cuisine);
        return ok(Utils.generateResponse("200", "Cuisine removed correctly", null));
    }
}

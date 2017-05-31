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

import java.util.*;

public class RestaurantController extends Controller {

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

    public Result getRestaurantsFromOwner(String page, String size, String seed){
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);
        final List<Restaurant> restaurants = owner.getRestaurants();
        Collections.shuffle(restaurants, new Random(Integer.parseInt(seed)));
        Map<String, Object> result = new HashMap<>();

        int start = Integer.parseInt(page) * Integer.parseInt(size);
        int end = start + Integer.parseInt(size);

        if (end >= restaurants.size()-1){
            result.put("hasNext", false);
            end = restaurants.size()-1;
        }else {
            result.put("hasNext", true);
        }
        final List<Restaurant> resultList = restaurants.subList(start, end);
        result.put("restaurants", resultList);
        return ok(Json.toJson(result));
    }

    public Result getRestaurant(String rid){
        final long id = Long.parseLong(rid);
        final Delivery delivery = Delivery.byId(id);
        if (delivery == null){
            final Local local = Local.getLocalById(id);
            if (local == null){
                return badRequest("local not found");
            }
            return ok(Json.toJson(local));
        }else {
            return ok(Json.toJson(delivery));
        }
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

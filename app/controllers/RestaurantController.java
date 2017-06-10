package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Response.DeliveryResponse;
import models.Response.LocalResponse;
import models.Response.RestaurantsResponse;
import models.requestObjects.PhotoObject;
import models.requestObjects.RestaurantEditObject;
import models.requestObjects.RestaurantObject;
import modules.ImageUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantController extends Controller {

    public Result addRestaurant(){
        final JsonNode jsonNode = request().body().asJson();
        final RestaurantObject restaurantObject = Json.fromJson(jsonNode, RestaurantObject.class);
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);
        if (restaurantObject.isLocal){
            final Local local = restaurantObject.toLocal(owner);
            final Photo photo = ImageUtils.saveImage(restaurantObject.photo);
            if (photo != null){
                local.save();
                photo.save();
                local.setPhoto(photo);
                local.update();
                owner.addRestaurant(local);
                owner.update();
                return ok(Json.toJson(local));
            }else{
                return badRequest("error al guardar la imagen");
            }
        } else {
            final Delivery delivery = restaurantObject.toDelivery(owner);
            final Photo photo = ImageUtils.saveImage(restaurantObject.photo);
            if (photo != null){
                photo.save();
                delivery.save();
                delivery.setPhoto(photo);
                delivery.update();
                owner.addRestaurant(delivery);
                owner.update();
                return ok(Json.toJson(delivery));
            }else{
                return badRequest("error al guardar la imagen");
            }
        }
    }

    public Result update(String rid){
        final JsonNode jsonNode = request().body().asJson();
        final RestaurantEditObject restaurantEditObject = Json.fromJson(jsonNode, RestaurantEditObject.class);
        final long id = Long.parseLong(rid);
        if (restaurantEditObject.isLocal){
            final Local local = Local.getLocalById(id);
            local.setCapacity(restaurantEditObject.capacity)
                    .setName(restaurantEditObject.name)
                    .setDescription(restaurantEditObject.description)
                    .setAddress(restaurantEditObject.address)
                    .setOpeningHour(restaurantEditObject.startTime)
                    .setClosingHour(restaurantEditObject.endTime)
                    .setCuisines(restaurantEditObject.cuisines)
                    .setOpeningDays(restaurantEditObject.days);

            local.update();
            return ok(Json.toJson(local));
        }else {
            final Delivery delivery = Delivery.byId(id);
            delivery.setRadius(restaurantEditObject.radius)
                    .setName(restaurantEditObject.name)
                    .setDescription(restaurantEditObject.description)
                    .setAddress(restaurantEditObject.address)
                    .setOpeningHour(restaurantEditObject.startTime)
                    .setClosingHour(restaurantEditObject.endTime)
                    .setCuisines(restaurantEditObject.cuisines)
                    .setOpeningDays(restaurantEditObject.days);
            delivery.update();
            return ok(Json.toJson(delivery));
        }
    }

    public Result getAll(){
        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        return ok(Json.toJson(ownerbyEmail.getRestaurants().stream().filter(p -> !p.isDeleted()).collect(Collectors.toList())));
    }

    public Result getFirsts(){
        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        final List<Restaurant> restaurants = ownerbyEmail.getRestaurants().stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
        int max = restaurants.size() < 5 ? restaurants.size() : 5;

        final RestaurantsResponse response = new RestaurantsResponse(200, "ok", restaurants.subList(0, max), restaurants.size() > 5);

        return ok(Json.toJson(response));
    }

    public Result changeState(String rid){
        final JsonNode body = request().body().asJson();
        final long id = Long.parseLong(rid);
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
            return ok(Json.toJson(new LocalResponse(local)));
        }else {
            return ok(Json.toJson(new DeliveryResponse(delivery)));
        }
    }

    public Result delete(String rid){
        final long id = Long.parseLong(rid);
        final Restaurant restaurant = Restaurant.byId(id);
        restaurant.setDeleted(true);
        restaurant.update();
        return ok("restaurant deleted");
    }

    public Result updatePhoto(String rid){
        final long id = Long.parseLong(rid);
        final JsonNode jsonNode = request().body().asJson();
        final PhotoObject photoObject = Json.fromJson(jsonNode, PhotoObject.class);
        final Photo newPhoto = ImageUtils.saveImage(photoObject);
        if (newPhoto == null){
            return badRequest();
        }
        final Restaurant restaurant = Restaurant.byId(id);
        final Photo oldPhoto = restaurant.getPhoto();
        if (!ImageUtils.deleteImage("./public/images/imgApp/" + oldPhoto.getName())){
            return badRequest();
        }
        newPhoto.save();
        restaurant.setPhoto(newPhoto);
        restaurant.update();
        return ok(Json.toJson(newPhoto));
    }
}

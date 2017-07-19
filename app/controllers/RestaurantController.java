package controllers;

import authentication.SecuredClient;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Response.DeliveryResponse;
import models.Response.LocalResponse;
import models.Response.RestaurantResponse;
import models.Response.RestaurantsResponse;
import models.requestObjects.PhotoObject;
import models.requestObjects.RestaurantEditObject;
import models.requestObjects.RestaurantObject;
import modules.ImageUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.*;
import play.mvc.Security;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantController extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public RestaurantController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result addRestaurant(){
        final JsonNode jsonNode = request().body().asJson();
        final RestaurantObject restaurantObject = Json.fromJson(jsonNode, RestaurantObject.class);
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);
        if (restaurantObject.isLocal){
            final Local local = restaurantObject.toLocal(owner);
            final Photo photo = ImageUtils.saveImage(restaurantObject.photo);
            return savePhoto(owner, local, photo);
        } else {
            final Delivery delivery = restaurantObject.toDelivery(owner);
            final Photo photo = ImageUtils.saveImage(restaurantObject.photo);
            return savePhoto(owner, delivery, photo);
        }
    }

    private Result savePhoto(Owner owner, Restaurant restaurant, Photo photo) {
        if (photo != null){
            restaurant.save();
            photo.save();
            restaurant.setPhoto(photo);
            restaurant.update();
            owner.addRestaurant(restaurant);
            owner.update();
            return ok(Json.toJson(restaurant));
        }else{
            return badRequest("error al guardar la imagen");
        }
    }

    public Result update(String rid){
        final JsonNode jsonNode = request().body().asJson();
        final RestaurantEditObject restaurantEditObject = Json.fromJson(jsonNode, RestaurantEditObject.class);
        final long id = Long.parseLong(rid);
        if (restaurantEditObject.isLocal){
            final Local local = Local.getLocalById(id);
            local.setCapacity(restaurantEditObject.capacity)
                    .setMinsBetweenTurns(restaurantEditObject.minsBetweenTurns)
                    .setName(restaurantEditObject.name)
                    .setDescription(restaurantEditObject.description)
                    .setOpeningHour(restaurantEditObject.startTime)
                    .setClosingHour(restaurantEditObject.endTime)
                    .setCuisines(restaurantEditObject.cuisines)
                    .setOpeningDays(restaurantEditObject.days)
                    .getAddress()
                    .setCompleteAddress(restaurantEditObject.address.completeAddress)
                    .setLng(restaurantEditObject.address.lng)
                    .setLat(restaurantEditObject.address.lat);

            restaurantEditObject.address.place.ifPresent(s -> local.getAddress().setPlace(s));
            restaurantEditObject.address.city.ifPresent(s -> local.getAddress().setCity(s));
            restaurantEditObject.address.state.ifPresent(s -> local.getAddress().setState(s));
            restaurantEditObject.address.country.ifPresent(s -> local.getAddress().setCountry(s));
            restaurantEditObject.address.district.ifPresent(s -> local.getAddress().setDistrict(s));

            local.update();
            return ok(Json.toJson(new LocalResponse(local)));
        }else {
            final Delivery delivery = Delivery.byId(id);
            delivery.setRadius(restaurantEditObject.radius)
                    .setResponseTime(restaurantEditObject.responseTime)
                    .setName(restaurantEditObject.name)
                    .setDescription(restaurantEditObject.description)
                    .setOpeningHour(restaurantEditObject.startTime)
                    .setClosingHour(restaurantEditObject.endTime)
                    .setCuisines(restaurantEditObject.cuisines)
                    .setOpeningDays(restaurantEditObject.days)
                    .getAddress()
                    .setCompleteAddress(restaurantEditObject.address.completeAddress)
                    .setLng(restaurantEditObject.address.lng)
                    .setLat(restaurantEditObject.address.lat);

            restaurantEditObject.address.place.ifPresent(s -> delivery.getAddress().setPlace(s));
            restaurantEditObject.address.city.ifPresent(s -> delivery.getAddress().setCity(s));
            restaurantEditObject.address.state.ifPresent(s -> delivery.getAddress().setState(s));
            restaurantEditObject.address.country.ifPresent(s -> delivery.getAddress().setCountry(s));
            restaurantEditObject.address.district.ifPresent(s -> delivery.getAddress().setDistrict(s));

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
        Collections.shuffle(restaurants);
        int max = restaurants.size() < 6 ? restaurants.size() : 6;

        final RestaurantsResponse response = new RestaurantsResponse(200, "ok", restaurants.subList(0, max), restaurants.size() > 6);

        return ok(Json.toJson(response));
    }

    public Result changeState(String rid){
        //I18N
        Messages messages = messagesApi.preferred(request());

        final JsonNode body = request().body().asJson();
        final long id = Long.parseLong(rid);
        final boolean state = body.get("state").asBoolean();
        final Restaurant restaurant = Restaurant.byId(id);
        restaurant.setPublished(state).update();

        if (state){
            String published = messages.at("restaurant.server.response.published.successfully", "");
            return ok(published);
        }
        String unpublished = messages.at("restaurant.server.response.unpublished.successfully","");
        return ok(unpublished);
        //return ok("State modified successfully");
    }

    public Result getRestaurantsFromOwner(){
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);
        final List<Restaurant> restaurants = owner.getRestaurants();
        Collections.shuffle(restaurants);
        Map<String, Object> result = new HashMap<>();
        result.put("hasNext", true);
        result.put("restaurants", restaurants.stream().map(r -> new RestaurantResponse(r, Qualification.getRestaurantQualification(r.getId()))).collect(Collectors.toList()));
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
        //I18N
        Messages messages = messagesApi.preferred(request());
        final long id = Long.parseLong(rid);
        final Restaurant restaurant = Restaurant.byId(id);
        restaurant.setDeleted(true);
        restaurant.update();
        String restaurantDeleted = messages.at("restaurant.server.response.restaurant.delete.successfully","");
        return ok(restaurantDeleted);
        //return ok("restaurant deleted");
    }

    public Result updatePhoto(String rid){
        //I18N
        Messages messages = messagesApi.preferred(request());

        final long id = Long.parseLong(rid);
        final JsonNode jsonNode = request().body().asJson();
        final PhotoObject photoObject = Json.fromJson(jsonNode, PhotoObject.class);
        final Photo newPhoto = ImageUtils.saveImage(photoObject);
        if (newPhoto == null){
            String error = messages.at("restaurant.server.response.an.error.occurs","");
            return badRequest(error);
        }
        final Restaurant restaurant = Restaurant.byId(id);
        final Photo oldPhoto = restaurant.getPhoto();
        if (oldPhoto != null) {
            if (!ImageUtils.deleteImage("./public/images/imgApp/" + oldPhoto.getName())) {
                String error = messages.at("restaurant.server.response.an.error.occurs", "");
                return badRequest(error);
            }
        }
        newPhoto.save();
        restaurant.setPhoto(newPhoto);
        restaurant.update();
        return ok(Json.toJson(newPhoto));
    }

    @Security.Authenticated(SecuredClient.class)
    public Result searchNearMe(String latString, String lngString) {
        double lat = Double.parseDouble(latString);
        double lng = Double.parseDouble(lngString);

        List<RestaurantResponse> result = Restaurant.allRestaurants().stream()
                .filter(restaurant -> !restaurant.isDeleted() && restaurant.isPublished())
                .filter(restaurant -> {
                    final double rLat = restaurant.getAddress().getLat();
                    final double rLng = restaurant.getAddress().getLng();
                    if (restaurant.isLocal()){
                        return distance(lat, lng, rLat, rLng) <= 5;
                    } else {
                        final Delivery delivery = Delivery.byId(restaurant.getId());
                        return distance(lat, lng, rLat, rLng) <= delivery.getRadius();
                    }
                })
                .map(r -> new RestaurantResponse(r, Qualification.getRestaurantQualification(r.getId())))
                .collect(Collectors.toList());
        return ok(Json.toJson(result));
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}

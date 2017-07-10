package controllers;

import authentication.SecuredClient;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Response.RestaurantResponse;
import play.libs.Json;
import play.mvc.*;

import java.util.*;
import java.util.stream.Collectors;

public class ClientHomeController extends Controller {

    private Long localSelectedId;

    public Result search(){
        JsonNode jsonNode = request().body().asJson();
        String wordToSearch = jsonNode.path("wordToSearch").asText();
        List<Restaurant> results = new ArrayList<>();

        /*Search by name*/
        List<Restaurant> searchedByName = Restaurant.finder.where()
                .icontains("name", wordToSearch).findList().stream().filter(restaurant -> !restaurant.isDeleted() && restaurant.isPublished()).collect(Collectors.toList());
        results.addAll(searchedByName);

        /*Search by cuisine*/
        //It must be change if a better (efficient) way of search is found.
        List<Restaurant> searchedByCuisine = Restaurant.finder.findList().stream().filter(restaurant -> !restaurant.isDeleted() && restaurant.isPublished()).collect(Collectors.toList());
        results.addAll(filterByCuisine(searchedByCuisine, wordToSearch));

        /*This is if the filter needs to be done in the server side, delete if not necessary
        boolean searchByName = jsonNode.path("filterByName").asBoolean();
        boolean searchByCuisine = jsonNode.path("filterByCuisine").asBoolean();
        boolean searchByLocalization = jsonNode.path("filterByLocalization").asBoolean();
        boolean searchByRestaurant = jsonNode.path("filterByRestaurant").asBoolean();
        boolean searchByDelivery = jsonNode.path("filterByDelivery").asBoolean();

        if(searchByName){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch).findList();
            results.addAll(aux);
        }
        if(searchByCuisine){
            //It must be change if a better (efficient) way of search is found.
            List<Restaurant> aux = Restaurant.finder.findList();
            results.addAll(filterByCuisine(aux, wordToSearch));
        }

        if(searchByLocalization){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch).findList();
        }
        if(searchByRestaurant){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch)
                    .eq("isLocal", true)
                    .findList();
            results.addAll(aux);
        }
        if(searchByDelivery){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch)
                    .eq("isLocal", false)
                    .findList();
            results.addAll(aux);
        }*/

        return ok(Json.toJson(results.stream().map(r -> new RestaurantResponse(r, Qualification.getRestaurantQualification(r.getId()))).collect(Collectors.toList())));
    }

    @Security.Authenticated(SecuredClient.class)
    public Result searchAllRestaurants(){
        return ok(Json.toJson(Restaurant.finder.where().findList().stream().filter(restaurant -> !restaurant.isDeleted() && restaurant.isPublished()).collect(Collectors.toList())));
    }

    public Result getRecommendations(){
        final String email = session().get("email");
        Client client = Client.getClientByEmail(email);

        final List<CuisinePreference> preferences = CuisinePreference.byClientId(client.getId()).stream().sorted((p1, p2) -> {
            if (p1.getAmount() > p2.getAmount())
                return -1;
            else if (p1.getAmount() == p2.getAmount()) {
                return 0;
            } else return 1;
        }).collect(Collectors.toList());

        if (preferences.size() == 0){
            return ok(Json.toJson(getRandomRestaurants(6, new ArrayList<>()).stream().map(r -> new RestaurantResponse(r, Qualification.getRestaurantQualification(r.getId()))).collect(Collectors.toList())));
        } else {
            List<Restaurant> restaurants = new ArrayList<>();
            int max = preferences.size() > 6 ? 6 : preferences.size();
            for (int i = 0; i < max; i++){
                setRandomRestaurantByCuisineToList(preferences.get(i).getCuisineId(), restaurants);
            }
            int randomAmount = 6 - preferences.size();
            if (randomAmount > 0) {
                getRandomRestaurants(6, restaurants);
            }
            return ok(Json.toJson(restaurants.stream().map(r -> new RestaurantResponse(r, Qualification.getRestaurantQualification(r.getId()))).collect(Collectors.toList())));
        }
    }

    private List<Restaurant> filterByCuisine(List<Restaurant> restaurants, String cuisine){
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant restaurant: restaurants) {
            List<Cuisine> cuisines = restaurant.getCuisines();
            Cuisine cuisineToSearch = Cuisine.getCuisine(getCorrectCuisineFormat(cuisine));
            if(cuisines.contains(cuisineToSearch)) result.add(restaurant);
        }
        return result;
    }

    //This method is use to get the correct format of the cuisines, the same format as it was saved.
    private String getCorrectCuisineFormat(String cuisine){
        String first = cuisine.substring(0,1).toUpperCase();
        String second = cuisine.substring(1, cuisine.length()).toLowerCase();
        return first+second;
    }

    private List<Restaurant> getRandomRestaurants(int maxQuantity, List<Restaurant> currentList){
        final List<Restaurant> all = Restaurant.finder.all().stream().filter(restaurant -> !restaurant.isDeleted() && restaurant.isPublished()).collect(Collectors.toList());
        while(currentList.size() < maxQuantity){
            int randomIndex = new Random().nextInt(all.size());
            final Restaurant restaurant = all.get(randomIndex);
            if (!currentList.contains(restaurant))
                currentList.add(restaurant);
        }
        return currentList;
    }

    private List<Restaurant> setRandomRestaurantByCuisineToList(long cuisineId, List<Restaurant> restaurants) {
        final List<Restaurant> byCuisine = Restaurant.getByCuisine(cuisineId);
        Collections.shuffle(byCuisine);
        int index = 0;
        while(index < byCuisine.size()) {
            if (!restaurants.contains(byCuisine.get(index))){
                restaurants.add(byCuisine.get(index));
                break;
            }
            index ++;
        }
        if (index == byCuisine.size()){
            getRandomRestaurants(restaurants.size(), restaurants);
        }
        return restaurants;
    }
}

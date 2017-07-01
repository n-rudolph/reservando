package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.xpath.internal.operations.Bool;
import models.Client;
import models.Cuisine;
import models.Restaurant;
import play.libs.Json;
import play.mvc.*;

import java.util.*;

public class ClientHomeController extends Controller {

    private Long localSelectedId;

    public Result search(){
        JsonNode jsonNode = request().body().asJson();
        String wordToSearch = jsonNode.path("wordToSearch").asText();
        List<Restaurant> results = new ArrayList<>();

        /*Search by name*/
        List<Restaurant> searchedByName = Restaurant.finder.where()
                .icontains("name", wordToSearch).findList();
        results.addAll(searchedByName);

        /*Search by cuisine*/
        //It must be change if a better (efficient) way of search is found.
        List<Restaurant> searchedByCuisine = Restaurant.finder.findList();
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

        return ok(Json.toJson(results));
    }

    public Result searchAllRestaurants(){
        return ok(Json.toJson(Restaurant.finder.where().findList()));
    }

    public Result getRecommendations(){
        JsonNode jsonNode = request().body().asJson();
        int quantity = jsonNode.path("quantity").asInt();
        String clientEmail = jsonNode.path("userEmail").asText();
        Client client = Client.getClientByEmail(clientEmail);
        List<Restaurant> result;

        //Search if the client has some recommendations. TO IMPLEMENT!
        if(true){
            result = getRecommendations(quantity,client);
        }
        //Else gives some randoms restaurants as recommendation.
        else result = getRandomRestaurants(quantity);


        return ok(Json.toJson(result));
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

    private List<Restaurant> getRandomRestaurants(int quantity){
        return Restaurant.finder.order().desc("id").setMaxRows(quantity).findList();
    }

    /*The get recommendations works with the top cuisines of the restaurants that the user looks*/
    private List<Restaurant> getRecommendations(int quantity, Client client){
        List<Long> restaurantsIds = new ArrayList<>();

        //To Test.
        restaurantsIds.add((long) 0);
        restaurantsIds.add((long) 2);
        restaurantsIds.add((long) 19);

        HashMap<Cuisine, Integer> cuisineQuantityMap = new HashMap<>();
        List<Restaurant> result = new ArrayList<>();

        for(Long restaurantId : restaurantsIds){
            Restaurant restaurant = Restaurant.finder.where().idEq(restaurantId).findUnique();
            if(restaurant != null){
                for(Cuisine cuisine: restaurant.getCuisines()){
                    if(cuisineQuantityMap.containsKey(cuisine)){
                        int previousValue = cuisineQuantityMap.get(cuisine);
                        cuisineQuantityMap.put(cuisine, previousValue + 1);
                    }
                    else {
                        cuisineQuantityMap.put(cuisine, 1);
                    }

                }
            }
        }

        List<Cuisine> mostViewCuisines = new ArrayList<>();
        for(int i = 0; i < quantity; i++){
            Cuisine mostView = new Cuisine();
            int mostViewCuisineValue = 0;
            Iterator<Cuisine> cuisineIterator = cuisineQuantityMap.keySet().iterator();
            if(cuisineIterator.hasNext()){
                while (cuisineIterator.hasNext()){
                    Cuisine currentCuisine = cuisineIterator.next();
                    int currentValue = cuisineQuantityMap.get(currentCuisine);
                    if (currentValue > mostViewCuisineValue){
                        mostViewCuisineValue = currentValue;
                        mostView = currentCuisine;
                    }
                }
                mostViewCuisines.add(mostView);
                cuisineQuantityMap.remove(mostView);
            }

        }

        List<Restaurant> allRestaurants = Restaurant.finder.where().findList();
        for(Restaurant restaurant : allRestaurants){
            if(result.size() == quantity) break;
            List<Cuisine> cuisines = restaurant.getCuisines();
            for (Cuisine mostViewCuisine : mostViewCuisines) {
                if (cuisines.contains(mostViewCuisine)) {
                    result.add(restaurant);
                    break;
                }
            }

        }

        return result;
    }
}

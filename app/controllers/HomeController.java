package controllers;

import models.Cuisine;
import models.Day;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's clientHome page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(login.render());
    }

    public Result clientHome() {return ok(clientHome.render());}
    public Result ownerHome() {return ok(ownerHome.render());}
    public Result newRestaurant(){return ok(newRestaurant.render());}
    public Result ownerRestaurants(){
        return ok(ownerRestaurants.render());
    }
    public Result ownerRestaurant() { return ok(ownerRestaurantProfile.render()); }

    public Result getDays() {
        return ok(Json.toJson(Day.all()));
    }
    public Result getCuisines() {
        return ok(Json.toJson(Cuisine.all()));
    }

    public Result clientRestaurant() {
        return ok(restaurant.render());
    }

}

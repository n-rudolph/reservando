package controllers;

import models.Cuisine;
import models.Day;
import play.libs.Json;
import play.api.i18n.*;
import play.mvc.*;
import views.html.*;
import javax.inject.Inject;

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

    private MessagesApi messagesApi;

    @Inject
    public HomeController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result index() {
        Messages messages = messagesApi.preferred(request());
        return ok(login.render(messages));
    }

    public Result clientHome() {
        Messages messages = messagesApi.preferred(request());
        return ok(clientHome.render(messages));
    }
    public Result ownerHome() {
        Messages messages = messagesApi.preferred(request());
        return ok(ownerHome.render(messages));
    }
    public Result newRestaurant(){
        Messages messages = messagesApi.preferred(request());
        return ok(newRestaurant.render(messages));
    }
    public Result ownerRestaurants(){
        Messages messages = messagesApi.preferred(request());
        return ok(ownerRestaurants.render(messages));
    }
    public Result ownerRestaurant() {
        Messages messages = messagesApi.preferred(request());
        return ok(ownerRestaurantProfile.render(messages));
    }
    public Result clientRestaurant() {
        Messages messages = messagesApi.preferred(request());
        return ok(restaurant.render(messages));
    }

    public Result getDays() {
        return ok(Json.toJson(Day.all()));
    }
    public Result getCuisines() {
        return ok(Json.toJson(Cuisine.all()));
    }

}

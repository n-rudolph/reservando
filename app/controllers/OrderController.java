package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.DeliveryOrder;
import models.requestObjects.OrderObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class OrderController extends Controller {

    public Result view(){
        return ok(newOrder.render());
    }

    public Result newOrder(){
        final JsonNode body = request().body().asJson();
        final OrderObject orderObject = Json.fromJson(body, OrderObject.class);
        final String email = session().get("email");
        final Client client = Client.byEmail(email);

        final DeliveryOrder deliveryOrder = orderObject.toOrder(client);
        deliveryOrder.save();

        return ok(Json.toJson(deliveryOrder));
    }

    public Result getOrder(String oId){
        return ok("");
    }

    public Result getClientOrders(String cId){
        return ok("");
    }

    public Result getRestaurantOrders(String rId){
        return ok("");
    }

    public Result getOwnerOrders(String oId){
        return ok("");
    }
}

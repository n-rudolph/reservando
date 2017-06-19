package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.DeliveryOrder;
import models.Response.OrderResponse;
import models.requestObjects.OrderObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderController extends Controller {

    public Result view(){
        return ok(newOrder.render());
    }
    public Result myOrdersView(){
        return ok(myOrders.render());
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
        final DeliveryOrder deliveryOrder = DeliveryOrder.byId(Long.parseLong(oId));
        final OrderResponse response = new OrderResponse(deliveryOrder);
        return ok(Json.toJson(response));
    }

    public Result getClientOrders(){
        final String email = session().get("email");
        final Client client = Client.byEmail(email);

        final List<OrderResponse> collect = DeliveryOrder.getClientOrders(client).stream().map(OrderResponse::new).collect(Collectors.toList());
        return ok(Json.toJson(collect));
    }

    public Result getRestaurantOrders(String rId){
        return ok("");
    }

    public Result getOwnerOrders(String oId){
        return ok("");
    }
}

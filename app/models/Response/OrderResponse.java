package models.Response;

import models.Delivery;
import models.DeliveryOrder;
import models.Discount;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    public long id;
    public String clientName;
    public int responseTime;
    public Delivery delivery;
    public List<MealOrderResponse> meals;
    public String address;
    public Discount discount;
    public DateTime timePlaced;

    public OrderResponse(DeliveryOrder order){
        this.id = order.getId();
        this.clientName = order.getClient().getFirstName() + " " + order.getClient().getlastName();
        this.delivery = order.getDelivery();
        this.responseTime = order.getDelivery().getResponseTime();
        this.meals = order.getMeals().stream().map(MealOrderResponse::new).collect(Collectors.toList());
        this.address = order.getAddress();
        this.discount = order.getDiscount();
        this.timePlaced = order.getTimePlaced();
    }
}

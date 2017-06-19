package models.Response;

import models.Client;
import models.Delivery;
import models.DeliveryOrder;
import models.Discount;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    public long id;
    public Client client;
    public Delivery delivery;
    public List<MealOrderResponse> meals;
    public String address;
    public Discount discount;

    public OrderResponse(DeliveryOrder order){
        this.id = order.getId();
        this.client = order.getClient();
        this.delivery = order.getDelivery();
        this.meals = order.getMeals().stream().map(MealOrderResponse::new).collect(Collectors.toList());
        this.address = order.getAddress();
        this.discount = order.getDiscount();
    }
}

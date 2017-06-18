package models.requestObjects;

import models.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderObject {
    public long dId;
    public List<MealOrderObject> menu;
    public String address;
    public String discountCode;

    public DeliveryOrder toOrder(Client client) {
        final Delivery delivery = Delivery.byId(dId);
        final Discount discount = Discount.byCode(discountCode);
        final List<MealOrder> mealOrders = menu.stream().filter(meal -> meal.amount > 0).map(MealOrderObject::toMealOrder).collect(Collectors.toList());
        if (discount != null) {
            discount.setUsed();
            discount.update();
            return new DeliveryOrder(client, delivery, mealOrders, address, discount);
        }else{
            return new DeliveryOrder(client, delivery, mealOrders, address, null);
        }
    }

}

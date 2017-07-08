package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.List;

@Entity
public class DeliveryOrder extends Model {
    @Id
    private long id;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Delivery delivery;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MealOrder> meals;
    private String address;
    @OneToOne
    @Nullable
    private Discount discount;
    private DateTime timePlaced;

    private static Finder<Long, DeliveryOrder> finder = new Finder<Long, DeliveryOrder>(DeliveryOrder.class);
    private boolean active;

    public DeliveryOrder() {
    }

    public DeliveryOrder(Client client, Delivery delivery, List<MealOrder> meals, String address, Discount discount) {
        this.timePlaced = DateTime.now();
        this.client = client;
        this.delivery = delivery;
        this.meals = meals;
        this.address = address;
        this.discount = discount;
        active = true;
    }

    public long getId() {
        return id;
    }

    public DeliveryOrder setId(long id) {
        this.id = id;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public DeliveryOrder setClient(Client client) {
        this.client = client;
        return this;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public DeliveryOrder setDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public List<MealOrder> getMeals() {
        return meals;
    }

    public DeliveryOrder setMeals(List<MealOrder> meals) {
        this.meals = meals;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public DeliveryOrder setAddress(String address) {
        this.address = address;
        return this;
    }

    @Nullable
    public Discount getDiscount() {
        return discount;
    }

    public DeliveryOrder setDiscount(@Nullable Discount discount) {
        this.discount = discount;
        return this;
    }

    public DateTime getTimePlaced() {
        return timePlaced;
    }

    public DeliveryOrder setTimePlaced(DateTime timePlaced) {
        this.timePlaced = timePlaced;
        return this;
    }

    public static List<DeliveryOrder> getClientOrders(Client client){
        return finder.where().eq("client", client).findList();
    }

    public static List<DeliveryOrder> all(){
        return finder.all();
    }

    public static DeliveryOrder byId(long id) {
        return finder.byId(id);
    }

    public static List<DeliveryOrder> getRestaurantOrders(Delivery delivery) {
        return finder.where().eq("delivery", delivery).findList();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}

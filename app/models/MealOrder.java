package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MealOrder extends Model{

    @Id
    private Long id;
    private Long mealId;
    private int amount;

    public MealOrder(Long mealId, int amount){
        this.mealId = mealId;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getMealId() {
        return mealId;
    }

    public MealOrder setMealId(Long mealId) {
        this.mealId = mealId;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public MealOrder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    private static Finder<Long, MealOrder> finder = new Finder<>(MealOrder.class);
}

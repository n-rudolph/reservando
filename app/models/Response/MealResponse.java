package models.Response;

import models.Meal;
import models.Photo;

public class MealResponse {

    public long id;
    public String name;
    public double price;
    public String description;
    public Photo photo;

    public MealResponse(Meal meal){
        this.id = meal.getId();
        this.name = meal.getName();
        this.price = meal.getPrice();
        this.description = meal.getDescription();

        if(meal.getImage() != null){
            final long photoId = meal.getImage().getId();
            this.photo = Photo.finder.byId(photoId);
        }

    }
}

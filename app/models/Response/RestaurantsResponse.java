package models.Response;


import models.Restaurant;

import java.util.List;

public class RestaurantsResponse extends ResponseObject{

    private List<Restaurant> list;
    private boolean continues;

    public RestaurantsResponse(int code, String msg, List<Restaurant> restaurants, boolean continues) {
        super(code, msg);
        list = restaurants;
        this.continues = continues;
    }

    public List<Restaurant> getList() {
        return list;
    }

    public boolean isContinues() {
        return continues;
    }
}

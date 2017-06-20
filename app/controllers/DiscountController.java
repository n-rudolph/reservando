package controllers;

import models.Discount;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class DiscountController extends Controller {

    public Result getDiscount(String code){
        final Discount discount = Discount.byCode(code);
        if (discount == null){
            return ok("1");
        } else if (discount.isUsed()) {
            return ok("2");
        }
        return ok(Json.toJson(discount));
    }
}

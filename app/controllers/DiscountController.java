package controllers;

import models.Discount;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.*;

public class DiscountController extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public DiscountController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result getDiscount(String code){
        //I18N
        Messages messages = messagesApi.preferred(request());

        final Discount discount = Discount.byCode(code);
        if (discount == null){
            String error = messages.at("discount.server.response.invalid.code", "");
            return badRequest(error);
            //return ok("1");
        } else if (discount.isUsed()) {
            String error = messages.at("discount.server.response.invalid.code", "");
            return badRequest(error);
            //return ok("2");
        }
        return ok(Json.toJson(discount));
    }
}

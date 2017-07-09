package controllers;

import play.api.i18n.Messages;
import play.api.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myOrders;
import views.html.newOrder;
import views.html.ownerMyOrders;

import javax.inject.Inject;

public class OrderControllerRender extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public OrderControllerRender(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result view(){
        Messages messages = messagesApi.preferred(request());
        return ok(newOrder.render(messages));
    }
    public Result myOrdersView(){
        Messages messages = messagesApi.preferred(request());
        return ok(myOrders.render(messages));
    }
    public Result ownerOrdersView() {
        Messages messages = messagesApi.preferred(request());
        return ok(ownerMyOrders.render(messages));
    }
}

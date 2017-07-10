package controllers;

import jsmessages.JsMessages;
import jsmessages.JsMessagesFactory;
import jsmessages.japi.Helper;
import play.libs.Scala;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessageController extends Controller {

    private JsMessages jsMessages;

    @Inject
    public MessageController(JsMessagesFactory jsMessagesFactory){
        this.jsMessages = jsMessagesFactory.all();
    }

    public Result messages(){
        return ok(jsMessages.apply(Scala.Option("window.Messages"), Helper.messagesFromCurrentHttpContext()));
    }
}

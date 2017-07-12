package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import jsmessages.JsMessages;
import jsmessages.JsMessagesFactory;
import jsmessages.japi.Helper;
import play.i18n.Lang;
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

    public Result getCurrentLang(){
        String currentLang = Controller.lang().toString();
        return ok(currentLang);
    }

    public Result changeLang(){
        JsonNode jsonNode = request().body().asJson();
        String lang = jsonNode.path("lang").asText();
        Controller.changeLang(lang);
        return ok();
    }
}

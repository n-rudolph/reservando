package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.requestObjects.UserRegisterObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.*;


import java.util.Collections;

public class RegisterController extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public RegisterController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result registerUser() {
        //I18N
        Messages messages = messagesApi.preferred(request());

        final JsonNode jsonNode = request().body().asJson();
        final UserRegisterObject registerObject = Json.fromJson(jsonNode, UserRegisterObject.class);
        if (!uniqueEmail(registerObject.email)) {
            String mailInUse = messages.at("register.server.response.email.in.use","");
            return badRequest(mailInUse);
            //return badRequest("El email ya se encuentra en uso");
        }
        if (!registerObject.userType) {
            Client aClient = registerObject.toClient();
            aClient.save();
        } else {
            Owner aOwner = registerObject.toOwner();
            aOwner.save();
        }
        session().clear();
        session().put("email", registerObject.email);

        if (!registerObject.userType)
            return ok("/client/home");
        return ok("/owner/home");
    }

    private boolean uniqueEmail(String email) {
        return !User.isEmailInUse(email);
    }
}

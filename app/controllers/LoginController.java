package controllers;

import authentication.SecuredUser;
import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Owner;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.*;
import play.mvc.Security;


public class LoginController extends Controller{

    private MessagesApi messagesApi;

    @Inject
    public LoginController(MessagesApi messagesApi){
        this.messagesApi =  messagesApi;
    }


    public Result login(){
        //I18N
        Messages messages = messagesApi.preferred(request());

        String email, password;
        final JsonNode jsonNode = request().body().asJson();
        if (jsonNode.path("email").isMissingNode()){
            String emailProblem = messages.at("login.server.response.email.problem","");
            return badRequest(emailProblem);
            //return badRequest("Ha occurido un problema con el email");
        }

        if (jsonNode.path("password").isMissingNode()) {
            String passwordProblem = messages.at("login.server.response.password.problem","");
            return badRequest(passwordProblem);
            //return badRequest("Ha occurido un problema con la contraseña");
        }

        email = jsonNode.path("email").asText();
        password = jsonNode.path("password").asText();
        final User userByEmail = User.getUserByEmail(email);
        if (userByEmail == null) {
            String emailNotValid = messages.at("login.server.response.email.not.valid","");
            return badRequest(emailNotValid);
            //return badRequest("El email no es valido");
        }
        if (!userByEmail.getPassword().equals(password)) {
            String passwordNotCorrect = messages.at("login.server.response.password.not.correct","");
            return badRequest(passwordNotCorrect);
            //return badRequest("La contraseña no es correcta");
        }

        session().clear();
        session().put("email", email);

        Client c = Client.getClientByEmail(email);
        if (c != null)
            return ok("/client/home");

        return ok("/owner/home");
    }

    @Security.Authenticated(SecuredUser.class)
    public Result logout(){
        session().clear();
        return redirect(routes.HomeController.index());
    }
}
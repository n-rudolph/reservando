package controllers;

import authentication.SecuredUser;
import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Owner;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;


public class LoginController extends Controller{

    public Result login(){
        String email, password;
        final JsonNode jsonNode = request().body().asJson();
        if (jsonNode.path("email").isMissingNode())
            return badRequest("Ha occurido un problema con el email");
        if (jsonNode.path("password").isMissingNode())
            return badRequest("Ha occurido un problema con la contraseña");
        email = jsonNode.path("email").asText();
        password = jsonNode.path("password").asText();
        final User userByEmail = User.getUserByEmail(email);
        if (userByEmail == null)
            return badRequest("El email no es valido");
        if (!userByEmail.getPassword().equals(password))
            return badRequest("La contraseña no es correcta");

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
package authentication;

import models.Client;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class SecuredClient extends Security.Authenticator{
    @Override
    public String getUsername(Http.Context ctx) {
        final String email = ctx.session().get("email");
        if(email == null) {
            return null;
        } else {
            final Client c = Client.getClientByEmail(email);
            if (c == null) {
                return null;
            }
            return c.getEmail();
        }
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        final String email = ctx.session().get("email");
        if (email == null) {
            return redirect("/");
        } else {
            return redirect("/owner/home");
        }
    }
}

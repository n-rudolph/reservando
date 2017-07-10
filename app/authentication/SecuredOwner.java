package authentication;

import models.Owner;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class SecuredOwner extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        final String email = ctx.session().get("email");
        if(email == null) {
            return null;
        } else {
            final Owner o = Owner.getOwnerbyEmail(email);
            if (o == null) {
                return null;
            }
            return o.getEmail();
        }
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        final String email = ctx.session().get("email");
        if (email == null) {
            return redirect("/");
        } else {
            return redirect("/client/home");
        }
    }
}

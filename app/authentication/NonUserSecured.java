package authentication;

import models.Client;
import models.Owner;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class NonUserSecured extends Security.Authenticator{

    @Override
    public String getUsername(Http.Context ctx) {
        if (ctx.session().get("email") == null){
            return "";
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        final String email = ctx.session().get("email");
        if (Client.byEmail(email) != null) {
            return redirect("/client/home");
        } else if(Owner.getOwnerbyEmail(email) != null) {
            return redirect("/owner/home");
        }
        return redirect("/");
    }
}

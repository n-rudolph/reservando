package authentication;

import models.Client;
import models.Owner;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class ActionAuthentication extends Action<Authenticate> {


    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        final String email = ctx.session().get("email");

        Client c = Client.getClientByEmail(email);
        Owner o = Owner.getOwnerbyEmail(email);

        if (c != null) {
            if (check(Client.class)){
                return delegate.call(ctx);
            }
            return CompletableFuture.supplyAsync(() -> {
                //todo: specify client clientHome page
                return redirect("/");
            });
        }
        if (o != null){
            if (check(Owner.class)){
                return delegate.call(ctx);
            }
            return CompletableFuture.supplyAsync(() -> {
                //todo: specify client clientHome page
                return redirect("/");
            });
        }
        return CompletableFuture.supplyAsync(() -> {
            //todo: specify client clientHome page
            return redirect("/");
        });
    }

    private boolean check(Class user) {
        Class[] users = configuration.value();
        for (Class user1 : users) {
            if (user1 == (user)) return true;
        }
        return false;
    }
}

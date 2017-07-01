package modules;

import com.google.inject.Inject;
import modules.Utilities.DataBasePopulator;
import play.inject.ApplicationLifecycle;

public class AppStartup {

    @Inject
    public AppStartup(ApplicationLifecycle lifecycle){
        onStart();
    }

    private void onStart(){
        DataBasePopulator dataBasePopulator = new DataBasePopulator();
        dataBasePopulator.populatePrimaryUsers();
        dataBasePopulator.populateLocals();
        dataBasePopulator.populateDeliveries();
        dataBasePopulator.populateDiscountCodes();
        dataBasePopulator.populateReservationsAndOrders();
    }
}

package modules;

import com.google.inject.Inject;
import modules.Utilities.DataBasePopulator;
import play.inject.ApplicationLifecycle;



/**
 * Created by Gustavo on 26/3/17.
 */
public class AppStartup {

    @Inject
    public AppStartup(ApplicationLifecycle lifecycle){
        onStart();
    }

    private void onStart(){
        DataBasePopulator dataBasePopulator = new DataBasePopulator();
        dataBasePopulator.populatePrimaryUsers();
        //For use populateLocals() it must be fix the restaurant database relationship.
        //dataBasePopulator.populateLocals();
    }
}

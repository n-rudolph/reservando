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
        dataBasePopulator.populateLocals();
    }
}

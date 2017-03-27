package modules;

import com.google.inject.AbstractModule;

/**
 * Created by Gustavo on 26/3/17.
 */
public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AppStartup.class).asEagerSingleton();
    }
}

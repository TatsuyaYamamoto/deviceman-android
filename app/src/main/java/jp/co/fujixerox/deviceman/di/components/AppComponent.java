package jp.co.fujixerox.deviceman.di.components;

import dagger.Component;
import jp.co.fujixerox.deviceman.App;
import jp.co.fujixerox.deviceman.di.modules.ActivityModule;
import jp.co.fujixerox.deviceman.di.modules.AppModule;

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);

    ActivityComponent plus(ActivityModule module);
}

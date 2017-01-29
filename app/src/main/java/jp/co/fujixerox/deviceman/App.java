package jp.co.fujixerox.deviceman;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import jp.co.fujixerox.deviceman.di.components.AppComponent;
import jp.co.fujixerox.deviceman.di.components.DaggerAppComponent;
import jp.co.fujixerox.deviceman.di.modules.AppModule;
import jp.co.fujixerox.deviceman.util.ActivityLifecycleHandler;
import lombok.Getter;

public class App extends Application {
    @Getter
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("App", "App onCreate!!");

        initializeInjector();

        if (BuildConfig.DEBUG) {
            setupDebugTools();
        }
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void setupDebugTools() {
        LeakCanary.install(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleHandler());
    }
}

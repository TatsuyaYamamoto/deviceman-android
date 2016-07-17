package jp.co.fujixerox.deviceman;

import android.app.Application;

import jp.co.fujixerox.deviceman.util.ActivityLifecycleHandler;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class App  extends Application{
    @Override
    public void onCreate(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleHandler());
    }

}

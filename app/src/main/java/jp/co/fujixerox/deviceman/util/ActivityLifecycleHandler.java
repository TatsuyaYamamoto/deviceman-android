package jp.co.fujixerox.deviceman.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class ActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static String TAG = ActivityLifecycleHandler.class.getName();
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + ": onActivityDestroyed");
    }
}

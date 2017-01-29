package jp.co.fujixerox.deviceman.presenter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import jp.co.fujixerox.deviceman.App;
import jp.co.fujixerox.deviceman.di.components.ActivityComponent;
import jp.co.fujixerox.deviceman.di.components.AppComponent;
import jp.co.fujixerox.deviceman.di.modules.ActivityModule;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getActivityComponent().inject(this);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link AppComponent}
     */
    protected ActivityComponent getActivityComponent() {
        return ((App) getApplication()).getAppComponent().plus(new ActivityModule());
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param messageStringResId resource id of a message to be shown.
     */
    protected void showToastMessage(@StringRes int messageStringResId) {
        Toast.makeText(this, messageStringResId, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * Get an Activity module for dependency injection.
//     *
//     * @return {@link com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule}
//     */
//    protected ActivityModule getActivityModule() {
//        return new ActivityModule(this);
//    }
}

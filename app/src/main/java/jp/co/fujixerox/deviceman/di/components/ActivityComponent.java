package jp.co.fujixerox.deviceman.di.components;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;
import jp.co.fujixerox.deviceman.App;
import jp.co.fujixerox.deviceman.di.modules.ActivityModule;
import jp.co.fujixerox.deviceman.di.modules.AppModule;
import jp.co.fujixerox.deviceman.presenter.activity.BaseActivity;
import jp.co.fujixerox.deviceman.presenter.activity.CheckoutSummaryActivity;
import jp.co.fujixerox.deviceman.presenter.activity.MainActivity;
import jp.co.fujixerox.deviceman.presenter.activity.QRScanActivity;
import jp.co.fujixerox.deviceman.presenter.activity.SelectOperationActivity;

@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(SelectOperationActivity activity);
    void inject(QRScanActivity activity);
    void inject(CheckoutSummaryActivity activity);
}

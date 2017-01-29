package jp.co.fujixerox.deviceman.presenter.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import jp.co.fujixerox.deviceman.service.SoundEffectPlayer;
import jp.co.fujixerox.deviceman.service.network.Apiclient;

@Module
public class ActivityModule {
    @Provides
    public Apiclient provideApiclient() {
        return new Apiclient();
    }
}

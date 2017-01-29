package jp.co.fujixerox.deviceman.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import jp.co.fujixerox.deviceman.SoundEffectPlayer;
import jp.co.fujixerox.deviceman.network.Apiclient;

@Module
public class ActivityModule {
    @Provides
    public Apiclient provideApiclient() {
        return new Apiclient();
    }

    @Provides
    public SoundEffectPlayer provideSoundEffectPlayer(Context context) {
        return new SoundEffectPlayer(context);
    }
}

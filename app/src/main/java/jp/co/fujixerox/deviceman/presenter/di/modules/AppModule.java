package jp.co.fujixerox.deviceman.presenter.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import jp.co.fujixerox.deviceman.App;

@Module
public class AppModule {
    private final Context mContext;

    public AppModule(App application) {


        System.out.println("----------- check!");
        this.mContext = application;
    }

    @Provides
    Context provideContext() {
        return this.mContext;
    }

//    @Provides
//    @Singleton
//    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
//        return jobExecutor;
//    }
//
//    @Provides
//    @Singleton
//    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
//        return uiThread;
//    }
//
//    @Provides
//    @Singleton
//    UserCache provideUserCache(UserCacheImpl userCache) {
//        return userCache;
//    }
//
//    @Provides
//    @Singleton
//    UserRepository provideUserRepository(UserDataRepository userDataRepository) {
//        return userDataRepository;
//    }
}

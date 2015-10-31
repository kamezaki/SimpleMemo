package biz.info_cloud.simplememo.di.module;

import android.app.Activity;

import javax.inject.Singleton;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.ui.navigation.Navigator;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return this.activity;
    }
}

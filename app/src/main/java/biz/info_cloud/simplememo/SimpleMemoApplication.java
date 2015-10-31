package biz.info_cloud.simplememo;

import android.app.Application;

import biz.info_cloud.simplememo.di.component.ApplicationComponent;
import biz.info_cloud.simplememo.di.component.DaggerApplicationComponent;
import biz.info_cloud.simplememo.di.module.ApplicationModule;

public class SimpleMemoApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        this.applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}

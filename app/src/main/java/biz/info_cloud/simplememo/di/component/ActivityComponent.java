package biz.info_cloud.simplememo.di.component;

import android.app.Activity;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.di.module.ActivityModule;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    // expose to sub-graph
    Activity activity();
}

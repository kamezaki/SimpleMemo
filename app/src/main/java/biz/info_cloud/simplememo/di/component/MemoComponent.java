package biz.info_cloud.simplememo.di.component;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.di.module.ActivityModule;
import biz.info_cloud.simplememo.ui.activity.MainActivity;
import biz.info_cloud.simplememo.ui.fragment.ContentListFragment;
import biz.info_cloud.simplememo.ui.fragment.EditFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface MemoComponent extends ActivityComponent {
    // activity
    void inject(MainActivity activity);

    // fragment
    void inject(ContentListFragment fragment);
    void inject(EditFragment fragment);
}

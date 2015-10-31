package biz.info_cloud.simplememo.di.component;

import android.content.Context;

import javax.inject.Singleton;

import biz.info_cloud.simplememo.SimpleMemoApplication;
import biz.info_cloud.simplememo.di.module.ApplicationModule;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import biz.info_cloud.simplememo.ui.activity.BaseActivity;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(SimpleMemoApplication application);

    // expose to sub-graph
    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    MemoRepository memoRepository();
}

package biz.info_cloud.simplememo.di.module;

import android.content.Context;

import javax.inject.Singleton;

import biz.info_cloud.simplememo.SimpleMemoApplication;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.JobExecutor;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import biz.info_cloud.simplememo.repository.realm.RealmMemoRepository;
import biz.info_cloud.simplememo.ui.UIThread;
import biz.info_cloud.simplememo.ui.navigation.Navigator;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ApplicationModule {
    private SimpleMemoApplication application;

    public ApplicationModule(SimpleMemoApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    public ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    public PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    public MemoRepository provideMemoRepository(RealmMemoRepository memoRepository) {
        return memoRepository;
    }
}

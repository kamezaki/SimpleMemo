package biz.info_cloud.simplememo.ui;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Singleton
public class UIThread implements PostExecutionThread {

    @Inject
    public UIThread() { }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}

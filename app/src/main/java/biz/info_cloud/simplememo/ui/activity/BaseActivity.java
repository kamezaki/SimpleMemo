package biz.info_cloud.simplememo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import biz.info_cloud.simplememo.SimpleMemoApplication;
import biz.info_cloud.simplememo.di.component.ApplicationComponent;
import biz.info_cloud.simplememo.di.component.DaggerMemoComponent;
import biz.info_cloud.simplememo.di.component.MemoComponent;
import biz.info_cloud.simplememo.di.module.ActivityModule;
import biz.info_cloud.simplememo.ui.navigation.Navigator;
import biz.info_cloud.simplememo.ui.presenter.Presenter;

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    private MemoComponent component;

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = initializeInjector();
        inject();

        if (getPresenter() != null) {
            getPresenter().initialize();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (getPresenter() != null) {
            getPresenter().resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getPresenter() != null) {
            getPresenter().pause();
        }
    }

    protected MemoComponent initializeInjector() {
        return DaggerMemoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return ((SimpleMemoApplication) getApplication()).getApplicationComponent();
    }


    public MemoComponent getComponent() {
        return component;
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected abstract void inject();
    protected abstract Presenter getPresenter();

}

package biz.info_cloud.simplememo.ui.presenter;

import javax.inject.Inject;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.ui.navigation.Navigator;

@PerActivity
public class MainPresenter implements Presenter {
    private Navigator navigator;

    @Inject
    MainPresenter(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void initialize() {
        navigator.openContentList();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openEditor(String memoId) {
        navigator.openEditor(memoId);
    }
}

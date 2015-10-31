package biz.info_cloud.simplememo.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biz.info_cloud.simplememo.di.HasComponent;
import biz.info_cloud.simplememo.di.component.MemoComponent;
import biz.info_cloud.simplememo.ui.activity.BaseActivity;
import biz.info_cloud.simplememo.ui.presenter.Presenter;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment{
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inject();
        initialize();
        if (getPresenter() != null) {
            getPresenter().initialize();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPresenter() != null) {
            getPresenter().pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().destroy();
        }
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());

    }

    protected MemoComponent getComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }


    protected abstract int getLayoutResourceId();
    protected abstract void inject();
    protected abstract void initialize();
    protected abstract Presenter getPresenter();
}

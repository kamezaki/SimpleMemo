package biz.info_cloud.simplememo.ui.navigation;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.info_cloud.simplememo.R;
import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.ui.activity.BaseActivity;
import biz.info_cloud.simplememo.ui.activity.MainActivity;
import biz.info_cloud.simplememo.ui.fragment.BaseFragment;
import biz.info_cloud.simplememo.ui.fragment.ContentListFragment;
import biz.info_cloud.simplememo.ui.fragment.EditFragment;
import biz.info_cloud.simplememo.util.StringUtil;


@PerActivity
public class Navigator {

    private Activity activity;

    @Inject
    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void openContentList() {
        ContentListFragment fragment = new ContentListFragment();
        startFragment(fragment, true);
    }

    public void openEditor(@Nullable String memoId) {
        EditFragment editFragment = new EditFragment();
        if (!StringUtil.isNullOrEmpty(memoId)) {
            Bundle bundle = new Bundle();
            bundle.putString(EditFragment.BUNDLE_MEMO_ID, memoId);
            editFragment.setArguments(bundle);
        }
        startFragment(editFragment, false);
    }

    public void back() {
        if (getFragmentManager().getBackStackEntryCount() < 1) {
            activity.finish();
            return;
        }
        getFragmentManager().popBackStackImmediate();
    }

    public void handleFloatingActionButton(boolean enable) {
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).enableFloatingActionButton(enable);
        }
    }

    private void startFragment(BaseFragment fragment, boolean first) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.main_content, fragment);
        if (!first) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    private FragmentManager getFragmentManager() {
        return activity.getFragmentManager();
    }

}

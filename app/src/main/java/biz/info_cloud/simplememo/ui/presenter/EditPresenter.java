package biz.info_cloud.simplememo.ui.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.usecase.FindMemoUseCase;
import biz.info_cloud.simplememo.domain.usecase.UpdateMemoUseCase;
import biz.info_cloud.simplememo.ui.navigation.Navigator;
import biz.info_cloud.simplememo.util.StringUtil;
import rx.Subscriber;

@PerActivity
public class EditPresenter implements Presenter {
    private static final String TAG = EditPresenter.class.getSimpleName();
    private Navigator navigator;
    private FindMemoUseCase findMemoUseCase;
    private UpdateMemoUseCase updateUseCase;
    private MvpView mvpView;
    private Memo memo = new Memo("", "");
    private boolean isDirty = false;

    @Inject
    public EditPresenter(Navigator navigator,
                         FindMemoUseCase findMemoUseCase,
                         UpdateMemoUseCase updateMemoUseCase) {
        this.navigator = navigator;
        this.findMemoUseCase = findMemoUseCase;
        this.updateUseCase = updateMemoUseCase;
    }

    public void setMvpView(MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void initialize() {
        navigator.handleFloatingActionButton(false);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.findMemoUseCase.unsubscribe();
        this.updateUseCase.unsubscribe();
    }

    private void setMemo(Memo memo) {
        this.memo = memo;
    }

    public void cancel() {
        navigator.back();
    }

    public void findMemo(String memoId) {
        if (StringUtil.isNullOrEmpty(memoId)) {
            return;
        }
        this.findMemoUseCase.execute(memoId, new FindMemoSubscriber());
    }

    public void updateMemo(String title, String content) {
        setTitleInternal(title);
        setContentInternal(content);
        if (this.isDirty) {
            this.updateUseCase.execute(this.memo, new UpdateSubscriber());
        }
    }

    private boolean setTitleInternal(String title) {
        if (memo.getTitle().equals(title)) {
            return false;
        }
        this.isDirty = true;
        if (title == null) {
            title = "";
        }
        memo.setTitle(title);
        return true;
    }
    public void setTitle(String title) {
        if (setTitleInternal(title)) {
            mvpView.showMemo(memo);
        }
    }

    private boolean setContentInternal(String content) {
        if (memo.getContent().equals(content)) {
            return false;
        }
        this.isDirty = true;
        if (content == null) {
            content = "";
        }
        memo.setContent(content);
        return true;
    }

    public void setContent(String content) {
        if (setContentInternal(content)) {
            mvpView.showMemo(memo);
        }
    }

    public void addTag(@NonNull String newTag) {
        memo.addTag(newTag);
        this.isDirty = true;
        mvpView.showMemo(memo);
    }

    public void deleteTag(@NonNull String deleteTag) {
        memo.removeTag(deleteTag);
        this.isDirty = true;
        mvpView.showMemo(memo);
    }

    private class FindMemoSubscriber extends Subscriber<Memo> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "find memo error", e);
        }

        @Override
        public void onNext(Memo memo) {
            if (memo != null) {
                setMemo(memo);
                mvpView.showMemo(memo);
            }
        }
    }

    private class UpdateSubscriber extends Subscriber<Memo> {

        @Override
        public void onCompleted() {
            navigator.back();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "update error", e);
        }

        @Override
        public void onNext(Memo memo) {
            setMemo(memo);
            isDirty = false;
        }
    }

    public interface MvpView {
        void showMemo(@NonNull Memo memo);
    }
}

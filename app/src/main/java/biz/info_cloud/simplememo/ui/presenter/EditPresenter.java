package biz.info_cloud.simplememo.ui.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.usecase.AddTagUseCase;
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
    private AddTagUseCase addTagUseCase;
    private MvpView mvpView;

    @Inject
    public EditPresenter(Navigator navigator,
                         FindMemoUseCase findMemoUseCase,
                         UpdateMemoUseCase updateMemoUseCase,
                         AddTagUseCase addTagUseCase) {
        this.navigator = navigator;
        this.findMemoUseCase = findMemoUseCase;
        this.updateUseCase = updateMemoUseCase;
        this.addTagUseCase = addTagUseCase;
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

    public void cancel() {
        navigator.back();
    }

    public void findMemo(String memoId) {
        if (StringUtil.isNullOrEmpty(memoId)) {
            return;
        }
        this.findMemoUseCase.execute(memoId, new FindMemoSubscriber());
    }

    public void update(@NonNull Memo newMemo) {
        this.updateUseCase.execute(newMemo, new UpdateSubscriber());
    }

    public void addTag(@NonNull String newTag, @NonNull Memo memo) {
        this.addTagUseCase.execute(newTag, memo, new UpdateTagsSubscriber());
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
                mvpView.showMemo(memo);
            }
        }
    }

    private class UpdateTagsSubscriber extends Subscriber<Memo> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "update tag error", e);
        }

        @Override
        public void onNext(Memo memo) {
            mvpView.showMemo(memo);
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

        }
    }

    public interface MvpView {
        void showMemo(@NonNull Memo memo);
    }
}

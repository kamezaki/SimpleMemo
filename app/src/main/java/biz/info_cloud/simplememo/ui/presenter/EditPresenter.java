package biz.info_cloud.simplememo.ui.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import biz.info_cloud.simplememo.domain.usecase.DefaultUseCase;
import biz.info_cloud.simplememo.domain.usecase.FindMemoUseCase;
import biz.info_cloud.simplememo.domain.usecase.UpdateMemoUseCase;
import biz.info_cloud.simplememo.ui.navigation.Navigator;
import biz.info_cloud.simplememo.util.StringUtil;
import rx.Observable;
import rx.Subscriber;

@PerActivity
public class EditPresenter implements Presenter {
    private Navigator navigator;
    private FindMemoUseCase findMemoUseCase;
    private UpdateMemoUseCase updateUsecase;
    private MvpView mvpView;

    @Inject
    public EditPresenter(Navigator navigator,
                         FindMemoUseCase findMemoUseCase,
                         UpdateMemoUseCase updateMemoUseCase) {
        this.navigator = navigator;
        this.findMemoUseCase = findMemoUseCase;
        this.updateUsecase = updateMemoUseCase;
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
        this.updateUsecase.unsubscribe();
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
        this.updateUsecase.execute(newMemo, new UpdateSubscriber());
    }

    private class FindMemoSubscriber extends Subscriber<Memo> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Memo memo) {
            if (memo != null) {
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

        }

        @Override
        public void onNext(Memo memo) {

        }
    }

    public interface MvpView {
        void showMemo(@NonNull Memo memo);
    }
}

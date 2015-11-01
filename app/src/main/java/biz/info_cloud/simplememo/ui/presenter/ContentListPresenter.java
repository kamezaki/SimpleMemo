package biz.info_cloud.simplememo.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.usecase.GetMemoListUseCase;
import biz.info_cloud.simplememo.ui.navigation.Navigator;
import rx.Subscriber;

@PerActivity
public class ContentListPresenter implements Presenter {
    private MvpView mvpView;
    private Navigator navigator;
    private GetMemoListUseCase getMemoListUseCase;


    @Inject
    public ContentListPresenter(Navigator navigator, GetMemoListUseCase getMemoListUseCase) {
        this.navigator = navigator;
        this.getMemoListUseCase = getMemoListUseCase;
    }

    public void setMvpView(MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void initialize() {
        navigator.handleFloatingActionButton(true);
    }

    @Override
    public void resume() {
        this.getMemoListUseCase.execute(new ContentListSubscriber());
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.getMemoListUseCase.unsubscribe();
    }

    public interface MvpView {
        void updateMemoList(List<Memo> memoList);
    }

    public void onListItemClicked(Memo memo) {
        navigator.openEditor(memo.getId());
    }

    private final class ContentListSubscriber extends Subscriber<List<Memo>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Memo> memoList) {
            mvpView.updateMemoList(memoList);
        }
    }
}

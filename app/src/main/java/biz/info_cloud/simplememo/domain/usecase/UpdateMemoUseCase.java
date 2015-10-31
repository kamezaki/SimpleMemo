package biz.info_cloud.simplememo.domain.usecase;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.info_cloud.simplememo.di.PerActivity;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import rx.Observable;
import rx.Subscriber;

public class UpdateMemoUseCase extends DefaultUseCase {
    private MemoRepository repository;
    private Memo memo;

    @Inject
    protected UpdateMemoUseCase(MemoRepository repository,
                                ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.repository.createOrUpdateMemo(this.memo);
    }

    public void execute(@NonNull Memo memo, @NonNull Subscriber<?> subscriber) {
        this.memo = memo;
        super.execute(subscriber);
    }
}

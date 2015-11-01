package biz.info_cloud.simplememo.domain.usecase;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import rx.Observable;
import rx.Subscriber;

public class FindMemoUseCase extends DefaultUseCase {
    private MemoRepository repository;
    private String memoId;

    @Inject
    protected FindMemoUseCase(MemoRepository repository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.repository.findMemo(this.memoId);
    }

    public void execute(@NonNull String memoId, Subscriber<?> subscriber) {
        this.memoId = memoId;
        execute(subscriber);
    }
}

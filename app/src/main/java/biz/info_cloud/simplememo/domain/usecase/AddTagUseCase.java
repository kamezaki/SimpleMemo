package biz.info_cloud.simplememo.domain.usecase;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import rx.Observable;
import rx.Subscriber;

public class AddTagUseCase extends DefaultUseCase {
    private MemoRepository repository;
    private String tagName;
    private Memo targetMemo;

    @Inject
    protected AddTagUseCase(MemoRepository repository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.repository.addTagToMemo(this.tagName, this.targetMemo);
    }

    public void execute(@NonNull String tagName, @NonNull Memo memo, Subscriber<?> subscriber) {
        this.tagName = tagName;
        this.targetMemo = memo;
        execute(subscriber);
    }
}

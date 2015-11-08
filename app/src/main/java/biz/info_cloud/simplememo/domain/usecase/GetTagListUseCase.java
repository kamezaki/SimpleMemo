package biz.info_cloud.simplememo.domain.usecase;

import javax.inject.Inject;

import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import rx.Observable;

public class GetTagListUseCase extends DefaultUseCase {
    private MemoRepository repository;

    @Inject
    protected GetTagListUseCase(MemoRepository repository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.getTagList();
    }
}

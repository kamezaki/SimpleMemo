package biz.info_cloud.simplememo.domain.usecase;

import javax.inject.Inject;

import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.executor.PostExecutionThread;
import biz.info_cloud.simplememo.domain.executor.ThreadExecutor;
import rx.Observable;

public class GetMemoListUseCase extends DefaultUseCase {
    private MemoRepository repository;

    @Inject
    protected GetMemoListUseCase(MemoRepository memoRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = memoRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.getMemoList(MemoRepository.SortOrder.DECEND);
    }
}

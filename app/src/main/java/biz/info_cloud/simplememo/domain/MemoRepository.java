package biz.info_cloud.simplememo.domain;

import java.util.List;

import rx.Observable;

public interface MemoRepository {
    Observable<List<Memo>> getMemoList(SortOrder orderByUpdate);
    Observable<Memo> createOrUpdateMemo(Memo newMemo);
    Observable<Memo> addTagToMemo(String tag, Memo memo);
    Observable<Memo> removeTagFromMemo(String tag, Memo memo);
    Observable<Memo> findMemo(String memoId);
    Observable<List<Tag>> getTagList();

    Observable<List<Tag>> findTag(String input);

    static enum SortOrder {
        ASCEND,
        DECEND
    }
}

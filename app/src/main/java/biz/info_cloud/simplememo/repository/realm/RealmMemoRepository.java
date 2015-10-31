package biz.info_cloud.simplememo.repository.realm;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.Tag;
import biz.info_cloud.simplememo.domain.exception.ItemNotFoundException;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;

@Singleton
public class RealmMemoRepository implements MemoRepository {
    private RealmConnection connection;
    private MemoDataMapper memoDataMapper;
    private TagDataMapper tagDataMapper;

    @Inject
    RealmMemoRepository(RealmConnection realmConnection,
                        MemoDataMapper memoDataMapper, TagDataMapper tagDataMapper) {
        this.connection = realmConnection;
        this.memoDataMapper = memoDataMapper;
        this.tagDataMapper = tagDataMapper;
    }

    @Override
    public Observable<List<Memo>> getMemoList(SortOrder orderByUpdate) {
        return Observable.create(subscriber -> {
            RealmQuery<RealmMemo> query = getRealm().where(RealmMemo.class);
            RealmResults<RealmMemo> queryResult = query.findAll();
            switch (orderByUpdate) {
                case ASCEND:
                    queryResult.sort("updateTimestamp", RealmResults.SORT_ORDER_ASCENDING);
                    break;
                case DECEND:
                default:
                    queryResult.sort("updateTimestamp", RealmResults.SORT_ORDER_DESCENDING);
                    break;
            }
            Observable.from(queryResult)
                    .map(realmMemo -> memoDataMapper.mapToDomain(realmMemo))
                    .toList()
                    .subscribe(subscriber);
        });
    }

    @Override
    public Observable<Memo> createOrUpdateMemo(Memo newMemo) {
        return Observable.create(subscriber -> {
            final Memo[] resultMemo = new Memo[1];
            getRealm().executeTransaction(realm -> {
                RealmMemo realmMemo = memoDataMapper.mapToRealm(newMemo);
                realmMemo.setUpdateTimestamp(Calendar.getInstance().getTimeInMillis());
                RealmMemo result = realm.copyToRealmOrUpdate(realmMemo);
                resultMemo[0] = memoDataMapper.mapToDomain(result);
            });
            subscriber.onNext(resultMemo[0]);
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Memo> addTagToMemo(String tag, Memo memo) {
        return Observable.create(subscriber -> {
            RealmMemo[] resultMemo = new RealmMemo[1];
            getRealm().executeTransaction(realm -> {
                RealmQuery<RealmMemo> query = realm.where(RealmMemo.class);
                resultMemo[0] = query.equalTo("uuid", memo.getId()).findFirst();
                if (resultMemo[0] != null) {
                    RealmTag targetRealmTag = new RealmTag();
                    targetRealmTag.setName(tag);
                    if (!resultMemo[0].getTags().contains(targetRealmTag)) {
                        resultMemo[0].getTags().add(targetRealmTag);
                    }
                }
            });
            if (resultMemo[0] != null) {
                subscriber.onNext(memoDataMapper.mapToDomain(resultMemo[0]));
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Memo> removeTagFromMemo(String tag, Memo memo) {
        return Observable.create(subscriber -> {
            RealmMemo[] resultMemo = new RealmMemo[1];
            getRealm().executeTransaction(realm -> {
                RealmQuery<RealmMemo> query = realm.where(RealmMemo.class);
                resultMemo[0] = query.equalTo("uuid", memo.getId()).findFirst();
                if (resultMemo[0] != null) {
                    RealmTag targetRealmTag = new RealmTag();
                    targetRealmTag.setName(tag);
                    if (resultMemo[0].getTags().contains(targetRealmTag)) {
                        resultMemo[0].getTags().remove(targetRealmTag);
                    }
                }
            });
            if (resultMemo[0] != null) {
                subscriber.onNext(memoDataMapper.mapToDomain(resultMemo[0]));
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Memo> findMemo(String memoId) {
        return Observable.create(subscriber -> {
            RealmQuery<RealmMemo> query = getRealm().where(RealmMemo.class);
            RealmMemo results = query.equalTo("uuid", memoId).findFirst();
            if (results != null) {
                subscriber.onNext(memoDataMapper.mapToDomain(results));
            } else {
                subscriber.onError(new ItemNotFoundException(String.format("memo [id : %s] not found", memoId)));
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<List<Tag>> findTag(String input) {
        return Observable.create(subscriber -> {
            RealmQuery<RealmTag> query = getRealm().where(RealmTag.class);
            RealmResults<RealmTag> results = query.beginsWith("name", input).findAll();
            results.sort("name", RealmResults.SORT_ORDER_ASCENDING);
            Observable.from(results)
                    .map(realmTag -> tagDataMapper.mapToDomain(realmTag))
                    .toList()
                    .subscribe(subscriber);
        });
    }

    private Realm getRealm() {
        return this.connection.getRealm();
    }
}

package biz.info_cloud.simplememo.repository.realm;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.MemoRepository;
import biz.info_cloud.simplememo.domain.Tag;
import biz.info_cloud.simplememo.domain.exception.ItemNotFoundException;
import biz.info_cloud.simplememo.util.StringUtil;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.subjects.AsyncSubject;

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
        return AsyncSubject.create(subscriber -> {
            Realm realm = null;
            try {
                realm = getRealm();
                RealmQuery<RealmMemo> query = realm.where(RealmMemo.class);
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
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                closeRealm(realm);
            }
        });
    }

    @Override
    public Observable<Memo> createOrUpdateMemo(Memo newMemo) {
        return AsyncSubject.create(subscriber -> {
            Realm realm = null;
            try {
                final Memo[] resultMemo = new Memo[1];
                realm = getRealm();
                realm.executeTransaction(r -> {
                    RealmMemo realmMemo = memoDataMapper.mapToRealm(newMemo);
                    realmMemo.setUpdateTimestamp(Calendar.getInstance().getTimeInMillis());
                    RealmMemo result = r.copyToRealmOrUpdate(realmMemo);
                    resultMemo[0] = memoDataMapper.mapToDomain(result);
                    if (newMemo.getTags() != null) {
                        Observable.from(newMemo.getTags())
                                .forEach(tag -> addTag(r, tag.getName(), resultMemo[0]));
                    }
                });
                subscriber.onNext(resultMemo[0]);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                closeRealm(realm);
            }
        });
    }

    private Memo addTag(Realm realm, String tag, Memo memo) {
        RealmMemo resultMemo;
        RealmQuery<RealmMemo> query = realm.where(RealmMemo.class);
        resultMemo = query.equalTo("uuid", memo.getId()).findFirst();
        if (resultMemo == null) {
            return null;
        }
        if (StringUtil.isNullOrEmpty(tag) || memo == null) {
            return null;
        }
        RealmTag targetRealmTag = new RealmTag();
        targetRealmTag.setName(tag);
        if (!resultMemo.getTags().contains(targetRealmTag)) {
            resultMemo.getTags().add(targetRealmTag);
        }
        return memoDataMapper.mapToDomain(resultMemo);
    }

    @Override
    public Observable<Memo> addTagToMemo(String tag, Memo memo) {
        return AsyncSubject.create(subscriber -> {
            Realm realm = null;
            try {
                realm = getRealm();
                Memo[] resultMemo = new Memo[1];
                realm.executeTransaction(r -> {
                    resultMemo[0] = addTag(r, tag, memo);
                });
                if (resultMemo[0] == null) {
                    throw new IllegalArgumentException("tag or memo is null");
                }
                subscriber.onNext(resultMemo[0]);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                closeRealm(realm);
            }
        });
    }

    @Override
    public Observable<Memo> removeTagFromMemo(String tag, Memo memo) {
        return AsyncSubject.create(subscriber -> {
            Realm realm = null;
            try {
                realm = getRealm();
                RealmMemo[] resultMemo = new RealmMemo[1];
                realm.executeTransaction(r -> {
                    RealmQuery<RealmMemo> query = r.where(RealmMemo.class);
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
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                closeRealm(realm);
            }
        });
    }

    @Override
    public Observable<Memo> findMemo(String memoId) {
        return AsyncSubject.create(subscriber -> {
            Realm realm = null;
            try {
                realm = getRealm();
                RealmQuery<RealmMemo> query = realm.where(RealmMemo.class);
                RealmMemo results = query.equalTo("uuid", memoId).findFirst();
                if (results != null) {
                    subscriber.onNext(memoDataMapper.mapToDomain(results));
                } else {
                    subscriber.onError(new ItemNotFoundException(String.format("memo [id : %s] not found", memoId)));
                }
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                closeRealm(realm);
            }
        });
    }

    @Override
    public Observable<List<Tag>> findTag(String input) {
        return AsyncSubject.create(subscriber -> {
            Realm realm = null;
            try {
                realm = getRealm();
                RealmQuery<RealmTag> query = realm.where(RealmTag.class);
                RealmResults<RealmTag> results = query.beginsWith("name", input).findAll();
                results.sort("name", RealmResults.SORT_ORDER_ASCENDING);
                Observable.from(results)
                        .map(realmTag -> tagDataMapper.mapToDomain(realmTag))
                        .toList()
                        .subscribe(subscriber);

            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                closeRealm(realm);
            }
        });
    }

    private Realm getRealm() {
        return this.connection.getRealm();
    }

    private void closeRealm(Realm realm) {
        if (realm != null) {
            realm.close();
        }
    }
}

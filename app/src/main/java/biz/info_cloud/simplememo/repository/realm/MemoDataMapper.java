package biz.info_cloud.simplememo.repository.realm;

import android.support.annotation.NonNull;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.domain.Tag;
import biz.info_cloud.simplememo.util.StringUtil;
import io.realm.RealmList;
import rx.Observable;

@Singleton
public class MemoDataMapper implements DataMapper<Memo,RealmMemo> {
    private  TagDataMapper tagDataMapper;

    @Inject
    MemoDataMapper(TagDataMapper tagDataMapper) {
        this.tagDataMapper = tagDataMapper;
    }

    @Override
    public Memo mapToDomain(@NonNull RealmMemo realmObject) {
        Memo memo = new Memo(realmObject.getUuid(), realmObject.getTitle(), realmObject.getContent());
        if (realmObject.getTags() != null) {
            Observable.from(realmObject.getTags())
                    .filter(realmTag -> !StringUtil.isNullOrEmpty(realmTag.getName()))
                    .map(realmTag -> tagDataMapper.mapToDomain(realmTag))
                    .toList()
                    .subscribe(tagList -> memo.addTag(tagList.toArray(new Tag[tagList.size()])));
        }
        return memo;
    }

    @Override
    public RealmMemo mapToRealm(@NonNull Memo domainObject) {
        RealmMemo realmMemo = new RealmMemo();
        realmMemo.setUuid(
                StringUtil.isNullOrEmpty(domainObject.getId())
                        ? UUID.randomUUID().toString() : domainObject.getId());
        realmMemo.setTitle(domainObject.getTitle());
        realmMemo.setContent(domainObject.getContent());
        realmMemo.setTags(new RealmList<>());
        Observable.from(domainObject.getTags())
                .filter(tag -> !StringUtil.isNullOrEmpty(tag.getName()))
                .map(tag -> tagDataMapper.mapToRealm(tag))
                .forEach(realmTag -> realmMemo.getTags().add(realmTag));

        return realmMemo;
    }
}

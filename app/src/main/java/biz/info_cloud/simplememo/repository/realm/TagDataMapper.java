package biz.info_cloud.simplememo.repository.realm;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.info_cloud.simplememo.domain.Tag;
import io.realm.Realm;

@Singleton
public class TagDataMapper implements DataMapper<Tag, RealmTag> {
    private Realm realm;

    @Inject
    TagDataMapper() {}

    @Override
    public Tag mapToDomain(@NonNull RealmTag realmObject) {
        return new Tag(realmObject.getName());
    }

    @Override
    public RealmTag mapToRealm(@NonNull Tag domainObject) {
        RealmTag realmTag = new RealmTag();
        realmTag.setName(domainObject.getName());
        return  null;
    }

}

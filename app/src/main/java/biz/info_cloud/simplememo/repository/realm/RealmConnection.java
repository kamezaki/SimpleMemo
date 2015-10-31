package biz.info_cloud.simplememo.repository.realm;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;


@Singleton
public class RealmConnection {
    private Context context;

    @Inject
    RealmConnection(Context context) {
        this.context = context;
    }

    public Realm getRealm() {
        return Realm.getInstance(context);
    }
}

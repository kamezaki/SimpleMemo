package biz.info_cloud.simplememo.repository.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmMemo extends RealmObject {

    @PrimaryKey
    private String uuid;

    @Required
    private String title;
    private String content;
    private RealmList<RealmTag> tags;
    private long updateTimestamp;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RealmList<RealmTag> getTags() {
        return tags;
    }

    public void setTags(RealmList<RealmTag> tags) {
        this.tags = tags;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}

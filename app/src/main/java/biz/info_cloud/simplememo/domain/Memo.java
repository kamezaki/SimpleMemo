package biz.info_cloud.simplememo.domain;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import biz.info_cloud.simplememo.util.StringUtil;
import rx.Observable;

public class Memo {
    private String id;
    private String title;
    private String content;
    private Set<Tag> tagList = new LinkedHashSet<>();

    public Memo(String title, String content) {
        this(null, title, content);
    }

    public Memo(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tagList);
    }

    public void addTag(Tag... tags) {
        if (tags != null && tags.length > 0) {
            Observable.from(tags)
                    .forEach(tag -> tagList.add(tag));
        }
    }

    public void addTag(String... tags) {
        if (tags != null && tags.length > 0) {
            Observable.from(tags)
                    .filter(tag -> StringUtil.isNullOrEmpty(tag))
                    .forEach(tag -> tagList.add(new Tag(tag)));
        }
    }

    public void removeTag(Tag... tags) {
        if (tags != null && tags.length > 0) {
            Observable.from(tags)
                    .forEach(tag -> tagList.remove(tag));
        }

    }

    public void removeTag(String... tags) {
        if (tags != null && tags.length > 0) {
            Observable.from(tags)
                    .filter(tag -> StringUtil.isNullOrEmpty(tag))
                    .forEach(tag -> tagList.remove(new Tag(tag)));
        }
    }
}

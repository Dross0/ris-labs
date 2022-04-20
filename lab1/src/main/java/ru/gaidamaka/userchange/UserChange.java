package ru.gaidamaka.userchange;

import ru.gaidamaka.service.osm.tag.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserChange {

    private final String user;

    private final String change;

    private final List<Tag> tags;

    public UserChange(String user, String change) {
        this.user = user;
        this.change = change;
        this.tags = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public String getChange() {
        return change;
    }


    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addAllTags(Collection<Tag> tags) {
        this.tags.addAll(tags);
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }
}

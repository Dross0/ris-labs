package ru.gaidamaka.service.osm.tag;

import java.util.Map;

public interface TagCounter {

    int increment(Tag tag);

    Map<Tag, Integer> getTags();

}

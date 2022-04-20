package ru.gaidamaka.service.osm.tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TagCounterImpl implements TagCounter {
    private final Map<Tag, Integer> tags = new HashMap<>();

    @Override
    public int increment(Tag tag) {
        tags.putIfAbsent(tag, 0);
        return tags.compute(tag, (s, counter) -> counter + 1);
    }

    @Override
    public Map<Tag, Integer> getTags() {
        return Collections.unmodifiableMap(tags);
    }
}

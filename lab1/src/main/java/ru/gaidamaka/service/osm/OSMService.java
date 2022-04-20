package ru.gaidamaka.service.osm;


import ru.gaidamaka.service.osm.tag.Tag;
import ru.gaidamaka.userchange.UsersChangeList;

import java.util.Map;

public interface OSMService {
    UsersChangeList getUsersChangeList();

    Map<Tag, Integer> getTags();
}

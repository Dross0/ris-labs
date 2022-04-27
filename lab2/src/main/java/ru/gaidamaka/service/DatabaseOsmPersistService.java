package ru.gaidamaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.DatabaseSchemaManager;
import ru.gaidamaka.db.dao.Dao;
import ru.gaidamaka.model.xml.Node;
import ru.gaidamaka.model.xml.Osm;
import ru.gaidamaka.model.xml.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class DatabaseOsmPersistService implements OsmPersistService {

    private final DatabaseSchemaManager schemaManager;

    private final Dao<Long, Node> nodeDao;

    private final Dao<Long, Tag> tagDao;

    @Override
    public void persist(Osm osm) {
        persist(osm, osm.getNode().size());
    }

    @Override
    public void persist(Osm osm, int limit) {
        schemaManager.drop();
        schemaManager.create();
        long start = System.nanoTime();
        List<Node> nodes = osm.getNode().subList(0, limit);
        List<Node> savedNodes = nodeDao.saveAll(nodes);
        List<Tag> tags = new ArrayList<>();
        for (Node node: savedNodes){
            node.getTag().forEach(tag -> tag.setNodeId(node.getId().longValue()));
            tags.addAll(node.getTag());
        }
        tagDao.saveAll(tags);
        long end = System.nanoTime();
        log.info("Time: {} ms", TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS));
    }
}

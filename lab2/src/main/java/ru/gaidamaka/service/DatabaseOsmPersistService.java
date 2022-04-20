package ru.gaidamaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.DatabaseSchemaManager;
import ru.gaidamaka.db.dao.Dao;
import ru.gaidamaka.model.xml.Node;
import ru.gaidamaka.model.xml.Osm;
import ru.gaidamaka.model.xml.Tag;

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
    public void persist(Osm osm, long limit) {
        schemaManager.drop();
        schemaManager.create();
        long start = System.nanoTime();
        for (int nodeIndex = 0; nodeIndex < limit; nodeIndex++){
            Node node = osm.getNode().get(nodeIndex);
            boolean createdNode = nodeDao.create(node);
            if (createdNode){
                for (Tag tag: node.getTag()){
                    tag.setNodeId(node.getId().longValue());
                    boolean tagCreated = tagDao.create(tag);
                    if (!tagCreated){
                        log.error("Ошибка сохранения tag='{}' для node с id='{}'", tag.getK(), tag.getNodeId());
                    }
                }
            } else {
                log.error("Ошибка сохранения node с id='{}'", node.getId());
            }
        }
        long end = System.nanoTime();
        log.info("Time: {}", TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS));
    }
}

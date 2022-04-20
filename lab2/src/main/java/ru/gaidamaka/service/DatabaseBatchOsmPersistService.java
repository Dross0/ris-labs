package ru.gaidamaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.DatabaseSchemaManager;
import ru.gaidamaka.db.dao.Dao;
import ru.gaidamaka.db.dao.NodeBatchDao;
import ru.gaidamaka.model.xml.Node;
import ru.gaidamaka.model.xml.Osm;
import ru.gaidamaka.model.xml.Tag;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class DatabaseBatchOsmPersistService implements OsmPersistService {

    private final DatabaseSchemaManager schemaManager;

    private final NodeBatchDao nodeBatchDao;

    private final Dao<Long, Tag> tagDao;

    @Override
    public void persist(Osm osm) {
        persist(osm, osm.getNode().size());
    }

    @Override
    public void persist(Osm osm, long limit) {
        schemaManager.drop();
        schemaManager.create();
        List<Node> nodes = osm.getNode().subList(0, (int) limit);
        long start = System.nanoTime();
        nodeBatchDao.createNodes(nodes);
        for (Node node: nodes){
            for (Tag tag: node.getTag()){
                tag.setNodeId(node.getId().longValue());
                boolean tagCreated = tagDao.create(tag);
                if (!tagCreated){
                    log.error("Ошибка сохранения tag='{}' для node с id='{}'", tag.getK(), tag.getNodeId());
                }
            }
        }
        long end = System.nanoTime();
        log.info("Time: {}", TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS));
    }
}

package ru.gaidamaka;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.archive.ArchiveConfiguration;
import ru.gaidamaka.archive.CliArchiveConfigParser;
import ru.gaidamaka.archive.config.ArchiveConfig;
import ru.gaidamaka.db.*;
import ru.gaidamaka.db.dao.*;
import ru.gaidamaka.model.xml.Node;
import ru.gaidamaka.model.xml.Osm;
import ru.gaidamaka.model.xml.Tag;
import ru.gaidamaka.service.DatabaseOsmPersistService;
import ru.gaidamaka.service.OsmPersistService;
import ru.gaidamaka.service.osm.OsmFactory;
import ru.gaidamaka.service.osm.OsmFactoryImpl;

import java.io.*;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static final int NODE_PERSIST_LIMIT = 200000;
    public static final String PROPERTY_FILE_NAME = "db.properties";

    public static void main(String[] args) {
        CompressorInputStream compressorInputStream = getCompressorInputStream(args);
        if (compressorInputStream == null) {
            return;
        }
        try (compressorInputStream) {
            OsmFactory osmFactory = new OsmFactoryImpl(compressorInputStream);
            Osm osm = osmFactory.createOsm();
            ConnectionManager connectionManager = new ConnectionManagerImpl(
                    new PropertiesDatabaseConfiguration(PROPERTY_FILE_NAME)
            );
            OsmPersistService persistService = getPersistServiceWithPreparedStatement(connectionManager);
            persistService.persist(osm, NODE_PERSIST_LIMIT);

            persistService = getPersistServiceWithString(connectionManager);
            persistService.persist(osm, NODE_PERSIST_LIMIT);

            persistService = getPersistServiceWithBatch(connectionManager);
            persistService.persist(osm, NODE_PERSIST_LIMIT);
        } catch (Exception e) {
            log.error("Ошибка при обработке данных", e);
        }
    }


    private static OsmPersistService getPersistServiceWithPreparedStatement(ConnectionManager connectionManager){
        return createOsmPersistService(connectionManager, new NodeDao(connectionManager));
    }

    private static OsmPersistService getPersistServiceWithString(ConnectionManager connectionManager){
        return createOsmPersistService(connectionManager, new NodeStringInsertDao(connectionManager));
    }

    private static OsmPersistService getPersistServiceWithBatch(ConnectionManager connectionManager){
        return createOsmPersistService(connectionManager, new NodeBatchDao(connectionManager));
    }

    private static OsmPersistService createOsmPersistService(ConnectionManager connectionManager, Dao<Long, Node> nodeDao){
        Dao<Long, Tag> tagDao = new TagDao(connectionManager);
        DatabaseSchemaManager schemaManager = new DatabaseSchemaManagerImpl(connectionManager);
        return new DatabaseOsmPersistService(schemaManager, nodeDao, tagDao);
    }


    private static CompressorInputStream getCompressorInputStream(String[] args) {
        ArchiveConfiguration archiveConfiguration = new CliArchiveConfigParser(args);
        ArchiveConfig config = archiveConfiguration.getConfig();
        try {
            return new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(config.getFilename()), config.getBufferSize()));
        } catch (IOException e) {
            log.error("Не удалось открыть архив '{}' с размером буфера '{}'", config.getFilename(), config.getBufferSize());
            return null;
        }
    }
}

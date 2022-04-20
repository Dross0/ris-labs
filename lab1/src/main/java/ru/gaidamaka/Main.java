package ru.gaidamaka;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.archive.ArchiveConfiguration;
import ru.gaidamaka.archive.CliArchiveConfigParser;
import ru.gaidamaka.archive.config.ArchiveConfig;
import ru.gaidamaka.service.osm.OSMService;
import ru.gaidamaka.service.osm.OSMServiceImpl;
import ru.gaidamaka.service.osm.tag.Tag;
import ru.gaidamaka.userchange.UsersChangeList;
import ru.gaidamaka.userchange.reader.UserChangeReader;
import ru.gaidamaka.userchange.reader.XMLUserChangeReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CompressorInputStream compressorInputStream = getCompressorInputStream(args);
        if (compressorInputStream == null) {
            return;
        }
        try (compressorInputStream) {
            UserChangeReader userChangeReader = new XMLUserChangeReader(compressorInputStream);

            OSMService osmService = new OSMServiceImpl(userChangeReader);

            printUserChangeList(osmService.getUsersChangeList());

            printTags(osmService.getTags());

        } catch (Exception e) {
            log.error("Ошибка при обработке данных", e);
        }
    }

    private static void printTags(Map<Tag, Integer> tags) {
        tags.forEach((tag, count) -> System.out.println("TAG - " + tag.getKey() + ": " + count));
    }

    private static void printUserChangeList(UsersChangeList usersChangeList) {
        usersChangeList.getChangesNumber()
                .forEach((user, countChanges) -> System.out.println(user + ": " + countChanges));
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

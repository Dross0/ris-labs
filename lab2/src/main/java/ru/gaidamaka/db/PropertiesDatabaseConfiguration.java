package ru.gaidamaka.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.config.DatabaseConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RequiredArgsConstructor
@Slf4j
public class PropertiesDatabaseConfiguration implements DatabaseConfiguration {
    private static final String URL_PROPERTY = "db.url";
    private static final String USER_PROPERTY = "db.user";
    private static final String PASSWORD_PROPERTY = "db.password";

    private final String propertyFileName;

    @Override
    public DatabaseConfig getConfig() {
        try (InputStream is = PropertiesDatabaseConfiguration.class
                .getClassLoader()
                .getResourceAsStream(propertyFileName)
        ){

            Properties properties = new Properties();
            properties.load(is);

            return new DatabaseConfig(
                    properties.getProperty(URL_PROPERTY),
                    properties.getProperty(USER_PROPERTY),
                    properties.getProperty(PASSWORD_PROPERTY)
            );
        } catch (IOException e) {
            log.error("Load database properties failed", e);
            return null;
        }
    }
}

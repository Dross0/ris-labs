package ru.gaidamaka.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.config.DatabaseConfig;
import ru.gaidamaka.exception.CreateConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class ConnectionManagerImpl implements ConnectionManager {

    private final DatabaseConfiguration databaseConfiguration;

    @Override
    public Connection getConnection() {
        DatabaseConfig databaseConfig = databaseConfiguration.getConfig();
        if (databaseConfig == null){
            throw new CreateConnectionException("Ошибка при получении параметров подключения к бд");
        }
        try {
            Connection connection = DriverManager.getConnection(
                    databaseConfig.getUrl(),
                    databaseConfig.getUser(),
                    databaseConfig.getPassword()
            );
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            log.error("Ошибка создания подключения к бд='{}' пользователя='{}'",
                    databaseConfig.getUrl(),
                    databaseConfig.getUser(),
                    e
            );
            throw new CreateConnectionException("Ошибка подключения к бд=" + databaseConfig.getUrl(), e);
        }
    }
}

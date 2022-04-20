package ru.gaidamaka.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.exception.SchemeException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
@Slf4j
public class DatabaseSchemaManagerImpl implements DatabaseSchemaManager {

    public static final String SQL_CREATE_NODE_TABLE =
            "CREATE TABLE IF NOT EXISTS nodes(id bigint primary key, lat double precision not null, " +
                    "lon double precision not null, userName text not null, uid bigint not null, " +
                    "visible boolean default false, version bigint not null, changeSet bigint not null, " +
                    "timeStamp timestamp not null);";

    public static final String SQL_CREATE_TAG_TABLE =
            "CREATE TABLE IF NOT EXISTS tags(id bigserial primary key, k text not null, " +
                    "v text not null, nodeId bigint references nodes(id));";

    public static final String SQL_DROP_NODE_TABLE = "DROP TABLE IF EXISTS nodes;";

    public static final String SQL_DROP_TAG_TABLE = "DROP TABLE IF EXISTS tags;";

    private final ConnectionManager connectionManager;

    @Override
    public void create() {
        try (Connection connection = connectionManager.getConnection()){
            try (Statement statement = connection.createStatement()) {
                statement.execute(SQL_CREATE_NODE_TABLE);
                statement.execute(SQL_CREATE_TAG_TABLE);
                connection.commit();
            }
        } catch (SQLException e) {
            throw new SchemeException("Ошибка создания схемы", e);
        }
    }

    @Override
    public void drop() {
        try (Connection connection = connectionManager.getConnection()){
            try (Statement statement = connection.createStatement()) {
                statement.execute(SQL_DROP_TAG_TABLE);
                statement.execute(SQL_DROP_NODE_TABLE);
                connection.commit();
            }
        } catch (SQLException e) {
            throw new SchemeException("Ошибка удаления схемы", e);
        }
    }
}

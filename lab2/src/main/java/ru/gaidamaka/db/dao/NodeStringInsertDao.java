package ru.gaidamaka.db.dao;

import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.ConnectionManager;
import ru.gaidamaka.exception.DataAccessException;
import ru.gaidamaka.model.xml.Node;

import java.sql.*;
import java.util.Locale;

@Slf4j
public class NodeStringInsertDao extends AbstractNodeDao{
    public static final String SQL_INSERT_NODE = "INSERT INTO nodes (id, lat, lon, userName, uid, visible, version, changeSet, timeStamp) " +
            "VALUES (%d, %f, %f, '%s', %d, %s, %d, %d, %s);";

    public NodeStringInsertDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public boolean create(Node entity) {
        try (Connection connection = connectionManager.getConnection()){
            try (Statement statement = connection.createStatement()) {
                String sql = String.format(
                        Locale.US,
                        SQL_INSERT_NODE,
                        entity.getId().longValue(),
                        entity.getLat(),
                        entity.getLon(),
                        entity.getUser(),
                        entity.getUid().longValue(),
                        entity.isVisible(),
                        entity.getVersion().longValue(),
                        entity.getChangeset().longValue(),
                        "to_timestamp('" + entity.getTimestamp() + "', 'yyyy-mm-ddThh24:mi:ssZ')"
                        );
                int rowsUpdated = statement.executeUpdate(sql);
                if (rowsUpdated == 1){
                    connection.commit();
                    return true;
                }
                connection.rollback();
                throw new DataAccessException("Ошибка добавления node с id=" + entity.getId().longValue());
            }
        } catch (SQLException e) {
            log.error("Ошибка при добавлении node с id='{}'", entity.getId().longValue(), e);
            throw new DataAccessException("Ошибка добавлениии node с id=" + entity.getId().longValue());
        }
    }
}

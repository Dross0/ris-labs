package ru.gaidamaka.db.dao;

import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.ConnectionManager;
import ru.gaidamaka.exception.DataAccessException;
import ru.gaidamaka.model.xml.Node;
import ru.gaidamaka.model.xml.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class NodeDao extends AbstractNodeDao {
    public static final String SQL_INSERT_NODE = "INSERT INTO nodes (id, lat, lon, userName, uid, visible, version, changeSet, timeStamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";


    public NodeDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public boolean create(Node entity) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_NODE)) {
                fillPreparedStatement(entity, preparedStatement);
                int rowsUpdated = preparedStatement.executeUpdate();
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

    @Override
    public List<Node> saveAll(Iterable<Node> nodes){
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_NODE)){
                List<Node> savedEntities = new ArrayList<>();
                for (Node node: nodes){
                    fillPreparedStatement(node, preparedStatement);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated == 1) {
                        savedEntities.add(node);
                    } else {
                        log.error("Ошибка сохранения узла={}", node);
                        connection.rollback();
                        return Collections.emptyList();
                    }
                }
                connection.commit();
                return savedEntities;
            }
        } catch (SQLException e) {
            log.error("Ошибка сохранения списка nodes", e);
            throw new DataAccessException("Ошибка сохранения списка nodes");
        }
    }

    protected void fillPreparedStatement(Node entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, entity.getId().longValue());
        preparedStatement.setDouble(2, entity.getLat());
        preparedStatement.setDouble(3, entity.getLon());
        preparedStatement.setString(4, entity.getUser());
        preparedStatement.setLong(5, entity.getUid().longValue());
        if (entity.isVisible() == null){
            preparedStatement.setNull(6, Types.BOOLEAN);
        } else {
            preparedStatement.setBoolean(6, entity.isVisible());
        }
        preparedStatement.setLong(7, entity.getVersion().longValue());
        preparedStatement.setLong(8, entity.getChangeset().longValue());
        Timestamp timestamp = new Timestamp(entity.getTimestamp().toGregorianCalendar().getTimeInMillis());
        preparedStatement.setTimestamp(9, timestamp);
    }
}

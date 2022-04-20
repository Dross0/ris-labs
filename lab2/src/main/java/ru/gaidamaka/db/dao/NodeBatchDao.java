package ru.gaidamaka.db.dao;

import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.ConnectionManager;
import ru.gaidamaka.exception.DataAccessException;
import ru.gaidamaka.model.xml.Node;

import java.sql.*;
import java.util.List;

@Slf4j
public class NodeBatchDao extends NodeDao{
    public static final String SQL_INSERT_NODE = "INSERT INTO nodes (id, lat, lon, userName, uid, visible, version, changeSet, timeStamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public NodeBatchDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    public boolean createNodes(List<Node> nodes){
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_NODE)) {
                for (Node node: nodes) {
                    fillPreparedStatement(node, preparedStatement);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                return true;
            }
        } catch (SQLException e) {
            log.error("Ошибка при добавлении nodes", e);
            throw new DataAccessException("Ошибка добавления nodes");
        }
    }
}

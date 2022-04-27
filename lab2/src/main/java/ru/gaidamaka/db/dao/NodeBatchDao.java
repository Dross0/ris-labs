package ru.gaidamaka.db.dao;

import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.ConnectionManager;
import ru.gaidamaka.exception.DataAccessException;
import ru.gaidamaka.model.xml.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NodeBatchDao extends NodeDao{
    public static final String SQL_INSERT_NODE = "INSERT INTO nodes (id, lat, lon, userName, uid, visible, version, changeSet, timeStamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public NodeBatchDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public List<Node> saveAll(Iterable<Node> nodes){
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_NODE)){
                List<Node> savedEntities = new ArrayList<>();
                for (Node node: nodes){
                    fillPreparedStatement(node, preparedStatement);
                    preparedStatement.addBatch();
                    savedEntities.add(node);
                }
                preparedStatement.executeBatch();
                connection.commit();
                return savedEntities;
            }
        } catch (SQLException e) {
            log.error("Ошибка сохранения списка nodes", e);
            throw new DataAccessException("Ошибка сохранения списка nodes");
        }
    }
}

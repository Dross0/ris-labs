package ru.gaidamaka.db.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.converter.XMLCalendarMapper;
import ru.gaidamaka.db.ConnectionManager;
import ru.gaidamaka.exception.DataAccessException;
import ru.gaidamaka.model.xml.Node;


import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractNodeDao implements Dao<Long, Node> {
    public static final String SQL_SELECT_ALL_NODES = "SELECT * FROM nodes;";

    public static final String SQL_SELECT_NODE_BY_ID = "SELECT * FROM nodes WHERE id=?;";

    public static final String SQL_DELETE_NODE_BY_ID = "DELETE FROM nodes WHERE id=?;";

    public static final String SQL_UPDATE_NODE_BY_ID = "UPDATE nodes SET lat=?, lon=?, userName=?," +
            " uid=?, visible=?, version=?, changeSet=?, timeStamp=? WHERE id=?;";


    protected final ConnectionManager connectionManager;

    @Override
    public List<Node> findAll() {
        try (Connection connection = connectionManager.getConnection()){
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_NODES)){
                    List<Node> nodes = new ArrayList<>();
                    while (resultSet.next()){
                        nodes.add(extractNode(resultSet));
                    }
                    return nodes;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка получения списка всех node", e);
        }
    }

    @Override
    public Optional<Node> findById(Long id){
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_NODE_BY_ID)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        return Optional.of(extractNode(resultSet));
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении node с id='{}'", id, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_NODE_BY_ID)) {
                preparedStatement.setLong(1, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                connection.commit();
                return rowsUpdated == 1;
            }
        } catch (SQLException e) {
            log.error("Ошибка при удалении node с id='{}'", id, e);
            return false;
        }
    }

    @Override
    public Node update(Node entity) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_NODE_BY_ID)) {
                preparedStatement.setDouble(1, entity.getLat());
                preparedStatement.setDouble(2, entity.getLon());
                preparedStatement.setString(3, entity.getUser());
                preparedStatement.setLong(4, entity.getUid().longValue());
                if (entity.isVisible() == null){
                    preparedStatement.setNull(5, Types.BOOLEAN);
                } else {
                    preparedStatement.setBoolean(5, entity.isVisible());
                }
                preparedStatement.setLong(6, entity.getVersion().longValue());
                preparedStatement.setLong(7, entity.getChangeset().longValue());
                Timestamp timestamp = new Timestamp(entity.getTimestamp().toGregorianCalendar().getTimeInMillis());
                preparedStatement.setTimestamp(8, timestamp);
                preparedStatement.setLong(9, entity.getId().longValue());
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 1){
                    connection.commit();
                    return entity;
                }
                connection.rollback();
                throw new DataAccessException("Ошибка обновление node с id=" + entity.getId().longValue());
            }
        } catch (SQLException e) {
            log.error("Ошибка при обновлении node с id='{}'", entity.getId().longValue(), e);
            throw new DataAccessException("Ошибка обновление node с id=" + entity.getId().longValue());
        }
    }

    protected Node extractNode(ResultSet resultSet) throws SQLException {
        Node node = new Node();
        node.setId(BigInteger.valueOf(resultSet.getLong("id")));
        node.setLat(resultSet.getDouble("lat"));
        node.setLon(resultSet.getDouble("lon"));
        node.setUser(resultSet.getString("userName"));
        node.setUid(BigInteger.valueOf(resultSet.getLong("uid")));
        node.setVisible(resultSet.getBoolean("visible"));
        node.setVersion(BigInteger.valueOf(resultSet.getLong("version")));
        node.setChangeset(BigInteger.valueOf(resultSet.getLong("changeSet")));

        XMLGregorianCalendar calendar = XMLCalendarMapper.fromLocalDateTime(
                resultSet.getTimestamp("timeStamp").toLocalDateTime()
        );

        node.setTimestamp(calendar);
        return node;
    }
}

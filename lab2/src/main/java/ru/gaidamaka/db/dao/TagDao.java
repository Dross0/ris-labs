package ru.gaidamaka.db.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gaidamaka.db.ConnectionManager;
import ru.gaidamaka.exception.DataAccessException;
import ru.gaidamaka.model.xml.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TagDao implements Dao<Long, Tag> {

    public static final String SQL_SELECT_ALL_TAGS = "SELECT * FROM tags;";

    public static final String SQL_SELECT_TAG_BY_ID = "SELECT * FROM tags WHERE id=?;";

    public static final String SQL_DELETE_TAG_BY_ID = "DELETE FROM tags WHERE id=?;";

    public static final String SQL_UPDATE_TAG_BY_ID = "UPDATE tags SET k=?, v=?, nodeId=? WHERE id=?;";

    public static final String SQL_INSERT_TAG = "INSERT INTO tags (k, v, nodeId) " +
            "VALUES (?, ?, ?);";

    private final ConnectionManager connectionManager;

    @Override
    public List<Tag> findAll() {
        try (Connection connection = connectionManager.getConnection()){
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_TAGS)){
                    List<Tag> tags = new ArrayList<>();
                    while (resultSet.next()){
                        tags.add(extractTag(resultSet));
                    }
                    return tags;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка получения списка всех tag", e);
        }
    }

    @Override
    public Optional<Tag> findById(Long id) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_TAG_BY_ID)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        return Optional.of(extractTag(resultSet));
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении tag с id='{}'", id, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_TAG_BY_ID)) {
                preparedStatement.setLong(1, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                connection.commit();
                return rowsUpdated == 1;
            }
        } catch (SQLException e) {
            log.error("Ошибка при удалении tag с id='{}'", id, e);
            return false;
        }
    }


    @Override
    public boolean create(Tag entity) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_TAG)) {
                fillTagPreparedStatement(entity, preparedStatement);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 1){
                    connection.commit();
                    return true;
                }
                connection.rollback();
                throw new DataAccessException("Ошибка вставки tag с id=" + entity.getId());
            }
        } catch (SQLException e) {
            log.error("Ошибка при вставке tag с id='{}'", entity.getId(), e);
            throw new DataAccessException("Ошибка вставки tag с id=" + entity.getId());
        }
    }

    @Override
    public Tag update(Tag entity) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_TAG_BY_ID)) {
                fillTagPreparedStatement(entity, preparedStatement);
                preparedStatement.setLong(4, entity.getId());
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 1){
                    connection.commit();
                    return entity;
                }
                connection.rollback();
                throw new DataAccessException("Ошибка обновление tag с id=" + entity.getId());
            }
        } catch (SQLException e) {
            log.error("Ошибка при обновлении tag с id='{}'", entity.getId(), e);
            throw new DataAccessException("Ошибка обновление tag с id=" + entity.getId());
        }
    }

    @Override
    public List<Tag> saveAll(Iterable<Tag> entities) {
        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_TAG)){
                List<Tag> savedEntities = new ArrayList<>();
                for (Tag tag: entities){
                    fillTagPreparedStatement(tag, preparedStatement);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated == 1) {
                        savedEntities.add(tag);
                    } else {
                        log.error("Ошибка сохранения тэга={}", tag);
                        connection.rollback();
                        return Collections.emptyList();
                    }
                }
                connection.commit();
                return savedEntities;
            }
        } catch (SQLException e) {
            log.error("Ошибка сохранения списка tags", e);
            throw new DataAccessException("Ошибка сохранения списка tags");
        }
    }

    private void fillTagPreparedStatement(Tag tag, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, tag.getK());
        preparedStatement.setString(2, tag.getV());
        preparedStatement.setLong(3, tag.getNodeId());
    }

    private Tag extractTag(ResultSet resultSet) throws SQLException {
        Tag tag = new Tag();
        tag.setK(resultSet.getString("k"));
        tag.setV(resultSet.getString("v"));
        return tag;
    }
}

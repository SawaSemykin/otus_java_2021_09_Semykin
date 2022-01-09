package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObj(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List<T> clientList = new ArrayList<>();
            try {
                while (rs.next()) {
                    T obj = createObj(rs);
                    clientList.add(obj);
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getParams(client));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getParams(client));
    }

    private List<Object> getParams(T client) {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(client);
                    } catch (IllegalAccessException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private T createObj(ResultSet rs) {
        try {
            T obj = entityClassMetaData.getConstructor().newInstance();
            for (Field f : entityClassMetaData.getAllFields()) {
                f.setAccessible(true);
                var value = rs.getObject(f.getName(), f.getType());
                f.set(obj, value);
            }
            return obj;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}

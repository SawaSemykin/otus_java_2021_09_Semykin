package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

/**
 * @author Aleksandr Semykin
 */
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from " + entityClassMetaData.getName() +
                " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        return "insert into " +
                entityClassMetaData.getName() +
                '(' + fieldsListForInsert() + ')' +
                " values (" + questionMarksList() + ')';
    }

    @Override
    public String getUpdateSql() {
        return "update" +
                entityClassMetaData.getName() +
                " set " + fieldsListForUpdate() +
                " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    private String fieldsListForInsert() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    private String questionMarksList() {
        StringBuilder result = new StringBuilder();
        result.append("?, ".repeat(entityClassMetaData.getFieldsWithoutId().size()));
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    private String fieldsListForUpdate() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(" = ?,"));
    }

}

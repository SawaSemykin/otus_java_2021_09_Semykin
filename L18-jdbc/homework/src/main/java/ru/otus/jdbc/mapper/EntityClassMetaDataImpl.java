package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksandr Semykin
 */
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final List<Field> allFields;
    private final Field idField;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> type) {
        try {
            this.name = type.getSimpleName().toLowerCase();
            this.constructor = type.getDeclaredConstructor();
            this.allFields = Arrays.asList(type.getDeclaredFields());
            this.idField = allFields.stream()
                    .filter(f -> f.getAnnotation(Id.class) != null)
                    .findAny()
                    .orElseThrow();
            this.fieldsWithoutId = allFields.stream()
                    .filter(f -> f.getAnnotation(Id.class) == null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}

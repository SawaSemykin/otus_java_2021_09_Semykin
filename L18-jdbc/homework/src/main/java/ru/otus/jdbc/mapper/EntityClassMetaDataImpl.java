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
    private final Class<T> type;

    public EntityClassMetaDataImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.getAnnotation(Id.class) != null)
                .findAny()
                .orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(type.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.getAnnotation(Id.class) == null)
                .collect(Collectors.toList());
    }
}

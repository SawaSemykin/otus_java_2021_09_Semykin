package ru.otus.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

/**
 * @author Aleksandr Semykin
 */
@Table("phone")
@Getter
@ToString
public class Phone {

    @Id
    private final Long id;

    @Nonnull
    private final String number;

    public Phone(String number) {
        this(null, number);
    }

    @PersistenceConstructor
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}

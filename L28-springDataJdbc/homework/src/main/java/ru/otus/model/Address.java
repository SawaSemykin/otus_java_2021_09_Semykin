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
@Table("address")
@Getter
@ToString
public class Address {

    @Id
    private final Long id;

    @Nonnull
    private final String street;

    public Address(String street) {
        this(null, street);
    }

    @PersistenceConstructor
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}

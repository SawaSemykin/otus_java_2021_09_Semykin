package ru.otus.core.repository;

import junit.framework.AssertionFailedError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class DataTemplateHibernateTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохраняет, изменяет и загружает клиента по заданному id")
    void shouldSaveAndFindCorrectClientById() {
        //given
        var address = new Address("address");
        var phones = List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"));
        var client = new Client(null, "Вася", address, phones);

        //when
        var savedClient = transactionManager.doInTransaction(session -> {
            clientTemplate.insert(session, client);
            return client;
        });

        //then
        assertThat(savedClient.getId()).isNotNull();
        assertThat(savedClient.getName()).isEqualTo(client.getName());

        //when
        var loadedSavedClient = transactionManager.doInReadOnlyTransaction(session ->{
                    var res = clientTemplate.findById(session, savedClient.getId())
                            .orElseThrow(() -> new AssertionFailedError("expected: not <null>"));
                    return Optional.ofNullable(res.clone());
                }
        );

        //then
        assertThat(loadedSavedClient).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);

        //when
        var updatedClient = savedClient.clone();
        updatedClient.setName("updatedName");
        transactionManager.doInTransaction(session -> {
            clientTemplate.update(session, updatedClient);
            return null;
        });

        //then
        var loadedClient = transactionManager.doInReadOnlyTransaction(session -> {
                    var res = clientTemplate.findById(session, updatedClient.getId())
                            .orElseThrow(() -> new AssertionFailedError("expected: not <null>"));

                    return Optional.of(res.clone());
                }
        );
        assertThat(loadedClient).isPresent().get().usingRecursiveComparison().isEqualTo(updatedClient);

        //when
        var clientList = transactionManager.doInReadOnlyTransaction(session ->
                clientTemplate.findAll(session).stream()
                        .map(Client::clone).collect(Collectors.toList())
        );

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0))
                .usingRecursiveComparison()
                .isEqualTo(updatedClient);


        //when
        clientList = transactionManager.doInReadOnlyTransaction(session ->
                clientTemplate.findByEntityField(session, "name", "updatedName")
                        .stream().map(Client::clone).collect(Collectors.toList())
        );

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0))
                .usingRecursiveComparison()
                .isEqualTo(updatedClient);
    }
}

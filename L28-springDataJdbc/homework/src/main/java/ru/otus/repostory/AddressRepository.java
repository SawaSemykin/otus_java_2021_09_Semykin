package ru.otus.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}

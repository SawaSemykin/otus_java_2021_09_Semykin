package ru.otus.crm.model;


import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name = "name")
    @Expose
    private String name;

    @OneToOne(
            fetch = FetchType.EAGER, // требуется по услововию тестов. Лучше LAZY, а инициализировать если нужно в сервисах или репозиториях
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="address_id")
    @Expose
    private Address address;

    @OneToMany(
            fetch = FetchType.EAGER, // см. выше
            mappedBy = "client",
            cascade = CascadeType.ALL
    )
    @Expose
    private Collection<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this(id, name);
        addAddress(address);
        phones.forEach(this::addPhone);
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setClient(this);
    }

    public void removePhone(Phone phone) {
        phones.remove(phone);
        phone.setClient(null);
    }

    public void addAddress(Address address) {
        removeAddress();
        this.address = address;
        address.setClient(this);
    }

    public void removeAddress() {
        if (address != null) {
            address.setClient(null);
            address = null;
        }
    }

    @Override
    public Client clone() {
        List<Phone> phoneCopiesList = phones.stream().map(Phone::new).collect(Collectors.toList());
        return new Client(this.id, this.name, new Address(address), phoneCopiesList);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Collection<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Collection<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

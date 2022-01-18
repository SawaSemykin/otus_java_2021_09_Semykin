package ru.otus.crm.model;

import javax.persistence.*;

/**
 * @author Aleksandr Semykin
 */
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    private String street;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Client client;

    public Address() {}

    public Address(String street) {
        this(null, street);
    }

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(Address address) {
        this(address.getId(), address.getStreet());
//        this.client = address.getClient(); // связь устанавливается в главной сущности Client
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", client=" + client +
                '}';
    }
}

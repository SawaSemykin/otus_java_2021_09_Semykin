package ru.otus.crm.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * @author Aleksandr Semykin
 */
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @Expose
    private Long id;

    @Expose
    private String number;

    @ManyToOne(fetch = FetchType.LAZY) // EAGER by default
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Phone() {}

    public Phone(String number) {
        this(null, number);
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(Phone phone) {
        this(phone.getId(), phone.getNumber());
        //this.client = phone.getClient(); // связь устанавливается в главной сущности Client
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", client=" + client +
                '}';
    }
}

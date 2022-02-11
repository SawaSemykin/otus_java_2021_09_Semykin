package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Aleksandr Semykin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    String name;
    String address;
    List<String> phones;
}

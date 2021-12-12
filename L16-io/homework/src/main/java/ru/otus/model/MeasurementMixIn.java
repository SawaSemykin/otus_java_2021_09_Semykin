package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Aleksandr Semykin
 */
public class MeasurementMixIn {
    MeasurementMixIn(@JsonProperty("name") String name,
                            @JsonProperty("value") double value) {}
}

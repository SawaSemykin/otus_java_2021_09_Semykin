package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final ObjectMapper mapper;
    private final File output;

    public FileSerializer(String fileName) {
        mapper = new ObjectMapper();
        output = new File(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) {
        serializeSafe(data);
    }

    private void serializeSafe(Map<String, Double> data) {
        try {
            mapper.writeValue(output, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

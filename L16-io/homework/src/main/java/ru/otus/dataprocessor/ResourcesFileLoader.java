package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementMixIn;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper mapper;
    private final File resourcesFile;

    public ResourcesFileLoader(String fileName){
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file '" + fileName + "' not found");
        } else {
            try {
                resourcesFile = new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }

        mapper = new ObjectMapper();
        mapper.addMixIn(Measurement.class, MeasurementMixIn.class);
    }

    @Override
    public List<Measurement> load() {
        return loadSafe();
    }

    private List<Measurement> loadSafe() {
        try {
            return Arrays.asList(mapper.readValue(resourcesFile, Measurement[].class));
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }
}

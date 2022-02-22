package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        try {
            dataBuffer.put(data);
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Waiting for queue space to become available was interrupted");
        }
    }

    public void flush() {
        try {
            if (!dataBuffer.isEmpty()) {
                List<SensorData> dataToBeFlushed = new ArrayList<>(bufferSize);
                dataBuffer.drainTo(dataToBeFlushed);
                dataToBeFlushed.sort(Comparator.comparing(SensorData::getMeasurementTime));
                writer.writeBufferedData(dataToBeFlushed);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}

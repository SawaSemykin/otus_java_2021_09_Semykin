package ru.otus.hw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aleksandr Semykin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {
    private String methodName;
    private boolean failed;
    private String failedReason;
}

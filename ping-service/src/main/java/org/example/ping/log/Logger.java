package org.example.ping.log;

import org.example.ping.constant.FileConstants;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Michael
 * @version 2024/6/23
 */
public class Logger {

    static {
        final File logFileDir = FileConstants.LOG_FILE_DIR;
        if (!logFileDir.exists()) {
            logFileDir.mkdir();
        }

        final File logFile = FileConstants.getLogFile();
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Logger() {

    }

    public static void log(String message) {
        final String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        Mono.just(message)
                .map(item -> datetime + ":" + message)
                .subscribe(logMessage -> {
                    try (FileWriter fileWriter = new FileWriter(FileConstants.getLogFile(), true)) {
                        fileWriter.write(logMessage);
                        fileWriter.write('\n');
                        fileWriter.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
    }
}

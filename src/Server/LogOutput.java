package Server;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LogOutput {
    private String message;

    LogOutput(String message) {
        this.message = message;
    }

    public void addLineToLog() throws IOException {
        Path p = Paths.get("./logs/" + LocalDate.now() + "_log.txt");
        try {
            Files.createFile(p);
        }
        catch (FileAlreadyExistsException ex) {
            System.err.println("File already exists");
        }
        finally {
            String log = LocalDate.now() + " - " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + " - " + this.message + " \n";
            Files.write(p, log.getBytes(), StandardOpenOption.APPEND);
        }

    }
}

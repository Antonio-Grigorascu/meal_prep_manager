package services;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static final String DIRECTORY = "logs";
    private static final String FILE_NAME = "audit.csv";
    private static final Path FILE_PATH = Paths.get(DIRECTORY, FILE_NAME);

    private static AuditService instance;

    private AuditService() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Eroare la inițializarea fișierului de audit: " + e.getMessage());
        }
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }


    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (FileWriter writer = new FileWriter(FILE_PATH.toFile(), true)) {
            writer.append(actionName)
                    .append(",")
                    .append(timestamp)
                    .append("\n");
        } catch (IOException e) {
            System.err.println("⚠️ Eroare la scrierea în fișierul de audit: " + e.getMessage());
        }
    }
}

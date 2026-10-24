package bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class CsvRideStore implements RideStore {
    private final Path path;

    public CsvRideStore(Path path) {
        this.path = path;
    }

    @Override
    public void append(int bikeId, String memberId, int minutes, long fee) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(bikeId + ",\"" + memberId.replace("\"", "\"\"") + "\"," + minutes + "," + fee + "\n");
        }
    }
}

package bikeshare;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public final class Main {
    public static void main(String[] args) {
        RideStore store = new CsvRideStore(Path.of("rides.csv"));
        BikeShareService service = new BikeShareService(store);
        try (Scanner input = new Scanner(System.in, StandardCharsets.UTF_8)) {
            new ConsoleMenu(service, System.out).run(input);
        }
    }
}

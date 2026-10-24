import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class BikeShare {
    public static String[] stations = {"Library", "Canteen", "Dorm"};
    public static int[] locations = {0, 0, 1, 1, 2, 2}; // -1 means rented
    public static String[] members = new String[6];
    public static int[] types = new int[6]; // 1 = student, 2 = staff

    public static void main(String[] args) {
        System.out.println("rent <memberId> <student|staff> <Library|Canteen|Dorm>");
        System.out.println("return <bike 1-6> <minutes> <Library|Canteen|Dorm>");
        System.out.println("Type quit to exit. Member IDs use letters, digits, _ or -.");
        try (Scanner input = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.equalsIgnoreCase("quit")) {
                    break;
                }
                if (line.isEmpty()) {
                    continue;
                }
                String[] words = line.split("\\s+");
                try {
                    if (words.length != 4 ||
                            !(words[0].equalsIgnoreCase("rent") || words[0].equalsIgnoreCase("return"))) {
                        throw new IllegalArgumentException("Use one of the commands shown above.");
                    }
                    ride(words, words[0].equalsIgnoreCase("return"));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                } catch (IOException | SecurityException e) {
                    System.out.println("Could not save ride; bike remains rented: " + e.getMessage());
                }
            }
            if (input.ioException() != null) {
                System.err.println("Keyboard read failed: " + input.ioException().getMessage());
            }
        }
    }

    public static void ride(String[] words, boolean returning) throws IOException {
        int station = -1;
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].equalsIgnoreCase(words[3])) {
                station = i;
                break;
            }
        }
        if (station == -1) {
            throw new IllegalArgumentException("Unknown station.");
        }
        if (returning) {
            int bike = Integer.parseInt(words[1]) - 1;
            int minutes = Integer.parseInt(words[2]);
            if (bike < 0 || bike >= locations.length || locations[bike] != -1 || minutes < 0) {
                throw new IllegalArgumentException("Choose a rented bike and nonnegative minutes.");
            }
            long fee = (long) minutes * (types[bike] == 1 ? 2 : 3);
            try (BufferedWriter file = Files.newBufferedWriter(Path.of("rides.csv"),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                file.write((bike + 1) + "," + members[bike] + "," + types[bike] + ","
                        + stations[station] + "," + minutes + "," + fee + "\n");
            }
            locations[bike] = station;
            members[bike] = null;
            types[bike] = 0;
            System.out.println("Receipt: bike " + (bike + 1) + ", " + minutes + " minutes, " + fee + " baht");
        } else {
            if (!words[1].matches("[A-Za-z0-9_-]+")) {
                throw new IllegalArgumentException("Invalid member ID.");
            }
            int type = words[2].equalsIgnoreCase("student") ? 1
                    : words[2].equalsIgnoreCase("staff") ? 2 : 0;
            if (type == 0) {
                throw new IllegalArgumentException("Member type must be student or staff.");
            }
            for (int bike = 0; bike < locations.length; bike++) {
                if (locations[bike] == station) {
                    locations[bike] = -1;
                    members[bike] = words[1];
                    types[bike] = type;
                    System.out.println("Rented bike " + (bike + 1));
                    return;
                }
            }
            System.out.println("No bikes available at " + stations[station]);
        }
    }
}

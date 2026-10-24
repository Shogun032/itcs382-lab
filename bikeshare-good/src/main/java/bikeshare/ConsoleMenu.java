package bikeshare;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

public final class ConsoleMenu {
    private final BikeShareService service;
    private final PrintStream output;

    public ConsoleMenu(BikeShareService service, PrintStream output) {
        this.service = service;
        this.output = output;
    }

    public void run(Scanner input) {
        output.println("rent <memberId> <student|staff> <Library|Canteen|Dorm>");
        output.println("return <bikeId> <minutes> <Library|Canteen|Dorm>");
        output.println("quit");
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            try {
                execute(line.split("\\s+"));
            } catch (IllegalArgumentException e) {
                output.println("Invalid input: " + e.getMessage());
            } catch (IOException | SecurityException e) {
                output.println("Save failed; bike remains rented: " + e.getMessage());
            }
        }
        if (input.ioException() != null) {
            output.println("Input failed: " + input.ioException().getMessage());
        }
    }

    private void execute(String[] words) throws IOException {
        if (words.length != 4) {
            throw new IllegalArgumentException("Expected a command and three arguments.");
        }
        Station station = Station.valueOf(words[3].toUpperCase(Locale.ROOT));
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "rent" -> {
                MemberType type = MemberType.valueOf(words[2].toUpperCase(Locale.ROOT));
                output.println("Rented bike " + service.rent(words[1], type, station));
            }
            case "return" -> {
                int bikeId = Integer.parseInt(words[1]);
                int minutes = Integer.parseInt(words[2]);
                long fee = service.returnBike(bikeId, minutes, station);
                output.println("Receipt: bike " + bikeId + ", " + minutes
                        + " minutes, " + fee + " baht");
            }
            default -> throw new IllegalArgumentException("Unknown command.");
        }
    }
}

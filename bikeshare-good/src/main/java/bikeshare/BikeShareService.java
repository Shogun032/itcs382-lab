package bikeshare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BikeShareService {
    private final List<Bike> bikes = new ArrayList<>();
    private final Map<Integer, String> renters = new HashMap<>();
    private final RideStore store;

    public BikeShareService(RideStore store) {
        this.store = Objects.requireNonNull(store);
        for (Station station : Station.values()) {
            bikes.add(new Bike(bikes.size() + 1, station));
            bikes.add(new Bike(bikes.size() + 1, station));
        }
    }

    public int rent(String memberId, MemberType type, Station station) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID must not be blank.");
        }
        Objects.requireNonNull(type, "Member type is required.");
        Objects.requireNonNull(station, "Station is required.");
        for (Bike bike : bikes) {
            if (bike.availableAt(station)) {
                bike.rent(type);
                renters.put(bike.id(), memberId);
                return bike.id();
            }
        }
        throw new IllegalArgumentException("No bikes available at " + station + ".");
    }

    public long returnBike(int bikeId, int minutes, Station destination) throws IOException {
        Objects.requireNonNull(destination, "Station is required.");
        Bike bike = bikes.stream().filter(item -> item.id() == bikeId).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown bike."));
        long fee = bike.returnFee(minutes);
        store.append(bikeId, renters.get(bikeId), minutes, fee);
        bike.finishReturn(destination);
        renters.remove(bikeId);
        return fee;
    }
}

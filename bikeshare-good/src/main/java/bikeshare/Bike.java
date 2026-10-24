package bikeshare;

final class Bike {
    private final int id;
    private Station station;
    private MemberType renterType;

    Bike(int id, Station station) {
        this.id = id;
        this.station = station;
    }

    int id() {
        return id;
    }

    boolean availableAt(Station requested) {
        return renterType == null && station == requested;
    }

    void rent(MemberType type) {
        if (renterType != null) {
            throw new IllegalArgumentException("Bike is already rented.");
        }
        renterType = type;
    }

    long returnFee(int minutes) {
        if (renterType == null) {
            throw new IllegalArgumentException("Bike is not rented.");
        }
        return renterType.fee(minutes);
    }

    void finishReturn(Station destination) {
        station = destination;
        renterType = null;
    }
}

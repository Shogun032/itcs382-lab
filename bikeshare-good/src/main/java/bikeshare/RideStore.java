package bikeshare;

import java.io.IOException;

public interface RideStore {
    void append(int bikeId, String memberId, int minutes, long fee) throws IOException;
}

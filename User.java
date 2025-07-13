import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    private String email;
    private List<Reservation> reservations;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public boolean cancelReservation(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equals(reservationId) && !res.isCancelled()) {
                res.cancelReservation();
                return true;
            }
        }
        return false;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Reservation> getReservations() {
        List<Reservation> activeReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (!res.isCancelled()) {
                activeReservations.add(res);
            }
        }
        return activeReservations;
    }
}
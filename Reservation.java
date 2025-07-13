import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private String reservationId;
    private User user;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private boolean isConfirmed;
    private boolean isCancelled;
    private double totalCost;

    public Reservation(String reservationId, User user, Room room, 
                      LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = reservationId;
        this.user = user;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isConfirmed = false;
        this.isCancelled = false;
        this.totalCost = calculateCost();
    }

    private double calculateCost() {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights * room.getPrice();
    }

    public void confirmReservation() {
        isConfirmed = true;
        room.setAvailable(false);
    }

    public void cancelReservation() {
        isCancelled = true;
        room.setAvailable(true);
    }

    // Getters
    public String getReservationId() { return reservationId; }
    public User getUser() { return user; }
    public Room getRoom() { return room; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public boolean isConfirmed() { return isConfirmed; }
    public boolean isCancelled() { return isCancelled; }
    public double getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        String status = isConfirmed ? "Confirmed" : "Pending";
        status = isCancelled ? "Cancelled" : status;
        return String.format(
            "Reservation %s\nUser: %s\nRoom: %s\nDates: %s to %s\nTotal: $%.2f\nStatus: %s",
            reservationId, user.getName(), room, checkIn, checkOut, totalCost, status
        );
    }
}
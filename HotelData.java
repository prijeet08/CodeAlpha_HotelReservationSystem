import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HotelData implements Serializable {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private Map<String, User> users;

    public HotelData(List<Room> rooms, List<Reservation> reservations, Map<String, User> users) {
        this.rooms = rooms;
        this.reservations = reservations;
        this.users = users;
    }

    // Getters
    public List<Room> getRooms() { return rooms; }
    public List<Reservation> getReservations() { return reservations; }
    public Map<String, User> getUsers() { return users; }
}
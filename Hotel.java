import java.time.LocalDate;
import java.util.*;

public class Hotel {
    private String name;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private Map<String, User> users; // userID -> User

    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.users = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        HotelData data = StorageManager.loadData();
        if (data != null) {
            this.rooms = data.getRooms();
            this.reservations = data.getReservations();
            this.users = data.getUsers();
        } else {
            // Initialize with sample data if no saved data exists
            addRoom(new StandardRoom("101", 99.99, 2));
            addRoom(new StandardRoom("102", 99.99, 2));
            addRoom(new DeluxeRoom("201", 149.99, 3));
            addRoom(new DeluxeRoom("202", 149.99, 3));
            addRoom(new Suite("301", 249.99, 4));
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public User registerUser(String userId, String name, String email) {
        User user = new User(userId, name, email);
        users.put(userId, user);
        saveData();
        return user;
    }

    public Reservation makeReservation(User user, String roomNumber, 
                                    LocalDate checkIn, LocalDate checkOut) {
        Room room = findAvailableRoom(roomNumber, checkIn, checkOut);
        if (room == null) {
            return null;
        }

        String reservationId = "RES-" + System.currentTimeMillis();
        Reservation reservation = new Reservation(reservationId, user, room, checkIn, checkOut);
        
        if (PaymentProcessor.processPayment(reservation.getTotalCost())) {
            reservation.confirmReservation();
            reservations.add(reservation);
            user.addReservation(reservation);
            saveData();
            return reservation;
        }
        return null;
    }

    public boolean cancelReservation(String userId, String reservationId) {
        User user = users.get(userId);
        if (user == null) return false;

        if (user.cancelReservation(reservationId)) {
            for (Reservation res : reservations) {
                if (res.getReservationId().equals(reservationId)) {
                    res.cancelReservation();
                    saveData();
                    return true;
                }
            }
        }
        return false;
    }

    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, int capacity) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCapacity() >= capacity && isRoomAvailable(room, checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    private Room findAvailableRoom(String roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber) && isRoomAvailable(room, checkIn, checkOut)) {
                return room;
            }
        }
        return null;
    }

    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!room.isAvailable()) return false;

        for (Reservation res : reservations) {
            if (res.getRoom().equals(room) && !res.isCancelled() &&
                !(checkOut.isBefore(res.getCheckIn()) || checkIn.isAfter(res.getCheckOut()))) {
                return false;
            }
        }
        return true;
    }

    public List<Reservation> getUserReservations(String userId) {
        User user = users.get(userId);
        return user != null ? user.getReservations() : Collections.emptyList();
    }

    private void saveData() {
        StorageManager.saveData(rooms, reservations, users);
    }

    // Getters
    public String getName() { return name; }
    public List<Room> getRooms() { return Collections.unmodifiableList(rooms); }
}
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

// ==================== ROOM CATEGORY ====================
abstract class Room implements Serializable {
    protected String roomNumber;
    protected double price;
    protected int capacity;
    protected boolean isAvailable;

    public Room(String roomNumber, double price, int capacity) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.isAvailable = true;
    }

    public abstract String getRoomType();

    // Getters and setters
    public String getRoomNumber() { return roomNumber; }
    public double getPrice() { return price; }
    public int getCapacity() { return capacity; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("%s Room %s ($%.2f/night, capacity: %d)", 
                getRoomType(), roomNumber, price, capacity);
    }
}

class StandardRoom extends Room {
    public StandardRoom(String roomNumber, double price, int capacity) {
        super(roomNumber, price, capacity);
    }

    @Override
    public String getRoomType() {
        return "Standard";
    }
}

class DeluxeRoom extends Room {
    public DeluxeRoom(String roomNumber, double price, int capacity) {
        super(roomNumber, price, capacity);
    }

    @Override
    public String getRoomType() {
        return "Deluxe";
    }
}

class Suite extends Room {
    public Suite(String roomNumber, double price, int capacity) {
        super(roomNumber, price, capacity);
    }

    @Override
    public String getRoomType() {
        return "Suite";
    }
}

// ==================== USER CATEGORY ====================
class User implements Serializable {
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

// ==================== RESERVATION CATEGORY ====================
class Reservation implements Serializable {
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

// ==================== PAYMENT CATEGORY ====================
class PaymentProcessor {
    public static boolean processPayment(double amount) {
        // Simulate payment processing
        System.out.printf("Processing payment of $%.2f...\n", amount);
        return true; // Always succeeds in simulation
    }
}

// ==================== STORAGE CATEGORY ====================
class HotelData implements Serializable {
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

class StorageManager {
    private static final String DATA_FILE = "hotel_data.ser";

    public static void saveData(List<Room> rooms, List<Reservation> reservations, 
                              Map<String, User> users) {
        HotelData data = new HotelData(rooms, reservations, users);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static HotelData loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (HotelData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return null;
        }
    }
}

// ==================== MAIN HOTEL SYSTEM ====================
class Hotel {
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

// ==================== MAIN APPLICATION ====================
public class HotelReservationSystem {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Grand Paradise");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. View My Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    searchRooms(hotel, scanner);
                    break;
                case 2:
                    makeReservation(hotel, scanner);
                    break;
                case 3:
                    viewReservations(hotel, scanner);
                    break;
                case 4:
                    cancelReservation(hotel, scanner);
                    break;
                case 5:
                    System.out.println("Thank you for using our system!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void searchRooms(Hotel hotel, Scanner scanner) {
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Enter required capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Room> availableRooms = hotel.searchAvailableRooms(checkIn, checkOut, capacity);
        
        System.out.println("\nAvailable Rooms:");
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the selected dates.");
        } else {
            availableRooms.forEach(System.out::println);
        }
    }

    private static void makeReservation(Hotel hotel, Scanner scanner) {
        System.out.print("Enter your user ID: ");
        String userId = scanner.nextLine();
        
        User user = hotel.registerUser(userId, "Guest User", userId + "@example.com");
        
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();
        
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());

        Reservation reservation = hotel.makeReservation(user, roomNumber, checkIn, checkOut);
        
        if (reservation != null) {
            System.out.println("\nReservation successful!");
            System.out.println(reservation);
        } else {
            System.out.println("Reservation failed. Room may not be available.");
        }
    }

    private static void viewReservations(Hotel hotel, Scanner scanner) {
        System.out.print("Enter your user ID: ");
        String userId = scanner.nextLine();
        
        List<Reservation> reservations = hotel.getUserReservations(userId);
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("\nYour Reservations:");
            reservations.forEach(System.out::println);
        }
    }

    private static void cancelReservation(Hotel hotel, Scanner scanner) {
        System.out.print("Enter your user ID: ");
        String userId = scanner.nextLine();
        
        System.out.print("Enter reservation ID to cancel: ");
        String reservationId = scanner.nextLine();
        
        if (hotel.cancelReservation(userId, reservationId)) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Failed to cancel reservation. It may not exist or already be cancelled.");
        }
    }
}
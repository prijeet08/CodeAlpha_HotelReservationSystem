import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HotelApp {
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
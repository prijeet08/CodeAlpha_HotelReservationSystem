import java.io.*;
import java.util.List;
import java.util.Map;

public class StorageManager {
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
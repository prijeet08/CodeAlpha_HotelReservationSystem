public class DeluxeRoom extends Room {
    public DeluxeRoom(String roomNumber, double price, int capacity) {
        super(roomNumber, price, capacity);
    }

    @Override
    public String getRoomType() {
        return "Deluxe";
    }
}
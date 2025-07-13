public class StandardRoom extends Room {
    public StandardRoom(String roomNumber, double price, int capacity) {
        super(roomNumber, price, capacity);
    }

    @Override
    public String getRoomType() {
        return "Standard";
    }
}
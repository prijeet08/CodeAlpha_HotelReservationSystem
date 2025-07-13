public class Suite extends Room {
    public Suite(String roomNumber, double price, int capacity) {
        super(roomNumber, price, capacity);
    }

    @Override
    public String getRoomType() {
        return "Suite";
    }
}
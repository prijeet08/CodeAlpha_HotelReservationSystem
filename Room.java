public abstract class Room {
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
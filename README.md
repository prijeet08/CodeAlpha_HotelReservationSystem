# CodeAlpha_HotelReservationSystem
markdown
# 🏨 Hotel Reservation System

A Java-based console application for managing hotel room bookings, cancellations, and availability with data persistence.

## ✨ Features

- **Room Categorization**: Standard, Deluxe, and Suite rooms
- **Booking Management**: Make, view, and cancel reservations
- **Payment Simulation**: Integrated payment processing
- **Data Persistence**: Save/load data using serialization
- **Search Functionality**: Find available rooms by date and capacity

## 🛠️ Technologies Used

- Java 17+
- Object-Oriented Programming (OOP) principles
- File I/O for data persistence
- Java Time API for date management

## 📦 Installation

1. Clone the repository:
```bash
git clone https://github.com/prijeet08/hotel-reservation-system.git
Compile and run:

bash
javac HotelReservationSystem.java
java HotelReservationSystem
🎮 Usage
text
=== Hotel Reservation System ===
1. Search Available Rooms
2. Make a Reservation
3. View My Reservations
4. Cancel Reservation
5. Exit
Choose an option: 
📂 Project Structure
text
hotel-reservation-system/
├── src/
│   ├── Room.java               # Abstract room class
│   ├── StandardRoom.java       # Standard room implementation
│   ├── DeluxeRoom.java         # Deluxe room implementation
│   ├── Suite.java              # Suite room implementation
│   ├── User.java               # User management
│   ├── Reservation.java        # Booking logic
│   ├── PaymentProcessor.java   # Payment handling
│   ├── StorageManager.java     # Data persistence
│   └── HotelReservationSystem.java # Main application
├── hotel_data.ser              # Data storage file
└── README.md                   # This file

🙏 Acknowledgments
CodeAlpha for the internship opportunity

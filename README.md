# 🚆 IRCTC - Train Ticket Booking System

A **command-line based Train Ticket Booking System** built in Java, inspired by the IRCTC platform. This application allows users to sign up, log in, search for trains, book seats, view bookings, and cancel tickets — all from the terminal.

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔐 **User Authentication** | Sign up & login with BCrypt password hashing |
| 🔍 **Train Search** | Search trains by source and destination stations |
| 💺 **Seat Booking** | View seat map and book available seats |
| 📋 **View Bookings** | Fetch and display all booked tickets |
| ❌ **Cancel Booking** | Cancel a booked ticket by ticket ID |
| 💾 **Persistent Storage** | User and train data stored in local JSON files |

---

## 🛠️ Tech Stack

- **Language:** Java 8
- **Build Tool:** Gradle 8.5
- **Libraries:**
  - [Jackson Databind](https://github.com/FasterXML/jackson-databind) – JSON serialization/deserialization
  - [jBCrypt](https://www.mindrot.org/projects/jBCrypt/) – Secure password hashing
  - [Lombok](https://projectlombok.org/) – Boilerplate code reduction
  - [Google Guava](https://github.com/google/guava) – Core Java utilities
- **Testing:** JUnit 4

---

## 📁 Project Structure

```
IRCTC/
├── app/
│   └── src/
│       ├── main/java/ticket/booking/
│       │   ├── App.java                  # Main entry point (CLI menu)
│       │   ├── entities/
│       │   │   ├── User.java             # User entity
│       │   │   ├── Train.java            # Train entity
│       │   │   └── Ticket.java           # Ticket entity
│       │   ├── service/
│       │   │   ├── UserBookingService.java   # User & booking operations
│       │   │   └── TrainService.java         # Train search & management
│       │   ├── util/
│       │   │   └── UserServiceUtil.java  # Password hashing utilities
│       │   └── localDB/
│       │       ├── users.json            # User data store
│       │       └── trains.json           # Train data store
│       └── test/java/ticket/booking/
│           └── AppTest.java              # Unit tests
├── gradle/                               # Gradle wrapper files
├── build.gradle                          # Build configuration
├── settings.gradle                       # Project settings
├── gradlew                               # Gradle wrapper (Unix)
├── gradlew.bat                           # Gradle wrapper (Windows)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 8** or higher (auto-downloaded via Gradle toolchain)
- **Git** (to clone the repository)

### Installation

1. **Clone the repository**
   ```bash
   git clone git@github.com:Arvindrana1/Ticket-Booking.git
   cd Ticket-Booking
   ```

2. **Build the project**
   ```bash
   # Linux / macOS
   ./gradlew build

   # Windows
   .\gradlew.bat build
   ```

3. **Run the application**
   ```bash
   # Linux / macOS
   ./gradlew run --console=plain

   # Windows
   .\gradlew.bat run --console=plain
   ```

---

## 📖 Usage

When you run the app, you'll see an interactive menu:

```
Running Train Booking System
Choose option
1. Sign up
2. Login
3. Fetch Bookings
4. Search Trains
5. Book a Seat
6. Cancel my Booking
7. Exit the App
```

### 1️⃣ Sign Up
Create a new account by providing a username and password. Passwords are securely hashed using BCrypt before storage.

### 2️⃣ Login
Log in with your registered username and password. The system verifies credentials against the stored BCrypt hash.

### 3️⃣ Fetch Bookings
View all your booked tickets after logging in.

### 4️⃣ Search Trains
Search for available trains by entering source and destination stations (e.g., `bangalore` → `delhi`).

### 5️⃣ Book a Seat
After searching, select a train and choose a seat from the seat map. Seats are displayed as a grid:
- `0` = Available
- `1` = Booked

### 6️⃣ Cancel Booking
Cancel an existing booking by entering the ticket ID.

### 7️⃣ Exit
Quit the application.

---

## 📊 Data Storage

The application uses local JSON files as a lightweight database:

- **`users.json`** — Stores user profiles, hashed passwords, and booked tickets
- **`trains.json`** — Stores train information including routes, schedules, and seat availability

---

## 🧪 Running Tests

```bash
# Linux / macOS
./gradlew test

# Windows
.\gradlew.bat test
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 👤 Author

**Arvind Rana**

---

<p align="center">
  Made with ❤️ in Java
</p>

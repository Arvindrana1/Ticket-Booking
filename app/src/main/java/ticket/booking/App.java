package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.service.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class App {

    public String getGreeting() {
        return "Running Train Booking System";
    }

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("There is something wrong");
            return;
        }

        Train trainSelectedForBooking = new Train();
        String sourceStation = "";
        String destStation = "";

        while (option != 7) {
            System.out.println("\nChoose option:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid line
                continue;
            }
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = readString(scanner);
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = readString(scanner);
                    User userToSignup = new User(nameToSignUp, passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(),
                            UUID.randomUUID().toString());
                    if (userBookingService.signUp(userToSignup)) {
                        System.out.println("Sign up successful! Please login.");
                    } else {
                        System.out.println("Sign up failed (username might already exist).");
                    }
                    break;
                case 2:
                    System.out.println("Enter the username to Login");
                    String nameToLogin = readString(scanner);
                    System.out.println("Enter the password to Login");
                    String passwordToLogin = readString(scanner);
                    User userToLogin = new User(nameToLogin, passwordToLogin,
                            UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(),
                            UUID.randomUUID().toString());
                    try {
                        UserBookingService tempService = new UserBookingService(userToLogin);
                        if (tempService.loginUser()) {
                            userBookingService = tempService;
                            System.out.println("Logged in successfully!");
                        } else {
                            System.out.println("Invalid login credentials!");
                        }
                    } catch (IOException ex) {
                        System.out.println("Error during login service initialization.");
                    }
                    break;
                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBookings();
                    break;
                case 4:
                    System.out.println("Type your source station");
                    sourceStation = readString(scanner);
                    System.out.println("Type your destination station");
                    destStation = readString(scanner);
                    List<Train> trains = userBookingService.getTrains(sourceStation, destStation);
                    if (trains.isEmpty()) {
                        System.out.println("No trains found between " + sourceStation + " and " + destStation);
                        break;
                    }
                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + " Train ID: " + t.getTrainId() + " Train No: " + t.getTrainNo());
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println(" station " + entry.getKey() + " time: " + entry.getValue());
                        }
                        index++;
                    }
                    System.out.println("Select a train by typing 1, 2, 3...");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid number format.");
                        scanner.nextLine(); // consume token
                        break;
                    }
                    int trainChoice = scanner.nextInt();
                    if (trainChoice >= 1 && trainChoice <= trains.size()) {
                        trainSelectedForBooking = trains.get(trainChoice - 1);
                        System.out.println("Selected Train: " + trainSelectedForBooking.getTrainId());
                    } else {
                        System.out.println("Invalid train selection.");
                    }
                    break;
                case 5:
                    if (trainSelectedForBooking == null || trainSelectedForBooking.getTrainId() == null) {
                        System.out.println("No train selected. Please search and select a train first using option 4.");
                        break;
                    }
                    System.out.println("Select a seat out of these seats:");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (int r = 0; r < seats.size(); r++) {
                        for (int c = 0; c < seats.get(r).size(); c++) {
                            System.out.print(r + "," + c + " (" + (seats.get(r).get(c) == 0 ? "O" : "X") + ")  ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column");
                    System.out.println("Enter the row:");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid row number.");
                        scanner.nextLine();
                        break;
                    }
                    int row = scanner.nextInt();
                    System.out.println("Enter the column:");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid column number.");
                        scanner.nextLine();
                        break;
                    }
                    int col = scanner.nextInt();
                    System.out.println("Enter date of travel (YYYY-MM-DD):");
                    String dateOfTravel = readString(scanner);
                    System.out.println("Booking your seat....");

                    String firstStation = trainSelectedForBooking.getStations().isEmpty() ? "Unknown"
                            : trainSelectedForBooking.getStations().get(0);
                    String lastStation = trainSelectedForBooking.getStations().isEmpty() ? "Unknown"
                            : trainSelectedForBooking.getStations()
                                    .get(trainSelectedForBooking.getStations().size() - 1);
                    String src = sourceStation.isEmpty() ? firstStation : sourceStation;
                    String dst = destStation.isEmpty() ? lastStation : destStation;

                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col, src, dst,
                            dateOfTravel);
                    if (booked.equals(Boolean.TRUE)) {
                        System.out.println("Booked! Enjoy your journey");
                    } else {
                        System.out.println("Can't book this seat");
                    }
                    break;
                case 6:
                    System.out.println("Enter the ticket ID to cancel:");
                    String ticketToCancel = readString(scanner);
                    Boolean canceled = userBookingService.cancelBooking(ticketToCancel);
                    if (canceled.equals(Boolean.TRUE)) {
                        System.out.println("Cancellation completed.");
                    } else {
                        System.out.println("Cancellation failed.");
                    }
                    break;
                case 7:
                    System.out.println("Thank you for using Train Booking System!");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }

    private static String readString(Scanner scanner) {
        String line = scanner.nextLine();
        while (line.trim().isEmpty()) {
            line = scanner.nextLine();
        }
        return line;
    }
}

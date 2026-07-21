package ticket.booking.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserBookingService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<User> userList;

    private User user;

    private final String USER_FILE_PATH = "app/src/main/java/ticket/booking/localDB/users.json";

    private File getUserFile() {
        File f = new File(USER_FILE_PATH);
        if (f.exists()) {
            return f;
        }
        String fallback = USER_FILE_PATH.substring(USER_FILE_PATH.indexOf("/") + 1);
        return new File(fallback);
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        userList = objectMapper.readValue(getUserFile(), new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName())
                    && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return true;
        }
        return false;
    }

    public Boolean signUp(User user1) {
        try {
            boolean exists = userList.stream().anyMatch(u -> u.getName().equalsIgnoreCase(user1.getName()));
            if (exists) {
                return Boolean.FALSE;
            }
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        objectMapper.writeValue(getUserFile(), userList);
    }

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName())
                    && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if (userFetched.isPresent()) {
            userFetched.get().printTickets();
        }
    }

    public Boolean cancelBooking(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        if (this.user == null) {
            System.out.println("Please login first to cancel a booking.");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;
        boolean removed = this.user.getTicketsBooked()
                .removeIf(ticket -> ticket.getTicketId().equalsIgnoreCase(finalTicketId1));

        if (removed) {
            try {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserId().equals(this.user.getUserId())) {
                        userList.set(i, this.user);
                        break;
                    }
                }
                saveUserListToFile();
                System.out.println("Ticket with ID " + ticketId + " has been canceled.");
                return Boolean.TRUE;
            } catch (IOException e) {
                System.out.println("Failed to save changes after cancellation.");
                return Boolean.FALSE;
            }
        } else {
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        String source = train.getStations().isEmpty() ? "Unknown" : train.getStations().get(0);
        String dest = train.getStations().isEmpty() ? "Unknown"
                : train.getStations().get(train.getStations().size() - 1);
        return bookTrainSeat(train, row, seat, source, dest, "2026-07-21");
    }

    public Boolean bookTrainSeat(Train train, int row, int seat, String source, String dest, String dateOfTravel) {
        try {
            if (this.user == null) {
                System.out.println("Please login first to book a seat.");
                return false;
            }
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    String ticketId = UUID.randomUUID().toString();
                    Ticket ticket = new Ticket(ticketId, this.user.getUserId(), source, dest, dateOfTravel, train);
                    if (this.user.getTicketsBooked() == null) {
                        this.user.setTicketsBooked(new ArrayList<>());
                    }
                    this.user.getTicketsBooked().add(ticket);

                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUserId().equals(this.user.getUserId())) {
                            userList.set(i, this.user);
                            break;
                        }
                    }
                    saveUserListToFile();
                    return true; // Booking successful
                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
    }
}

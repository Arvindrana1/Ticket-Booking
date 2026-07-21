package ticket.booking.service;

import org.junit.Before;
import org.junit.Test;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.entities.Ticket;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserBookingServiceTest {

    private UserBookingService userBookingService;
    private User testUser;

    @Before
    public void setUp() throws IOException {
        String uniqueName = "testuser_" + UUID.randomUUID().toString().substring(0, 8);
        testUser = new User(uniqueName, "password", UserServiceUtil.hashPassword("password"), new ArrayList<>(),
                UUID.randomUUID().toString());
        userBookingService = new UserBookingService(testUser);
    }

    @Test
    public void testSignUpAndLogin() {
        Boolean signUpResult = userBookingService.signUp(testUser);
        assertTrue("Sign up should succeed for new user", signUpResult);

        Boolean loginResult = userBookingService.loginUser();
        assertTrue("Login should succeed with correct credentials", loginResult);
    }

    @Test
    public void testSearchTrains() {
        List<Train> trains = userBookingService.getTrains("bangalore", "delhi");
        assertNotNull("Train list should not be null", trains);
        assertFalse("Should find at least one train between bangalore and delhi", trains.isEmpty());
    }

    @Test
    public void testBookAndCancelSeat() {
        System.out.println("TEST USER: " + testUser.getName());
        boolean signupOk = userBookingService.signUp(testUser);
        System.out.println("SIGN UP OK: " + signupOk);
        boolean loginOk = userBookingService.loginUser();
        System.out.println("LOGIN OK: " + loginOk);

        List<Train> trains = userBookingService.getTrains("bangalore", "delhi");
        System.out.println("TRAINS COUNT: " + trains.size());
        if (!trains.isEmpty()) {
            Train train = trains.get(0);

            // Find an empty seat dynamically
            int targetRow = -1;
            int targetCol = -1;
            List<List<Integer>> seats = train.getSeats();
            for (int r = 0; r < seats.size(); r++) {
                for (int c = 0; c < seats.get(r).size(); c++) {
                    if (seats.get(r).get(c) == 0) {
                        targetRow = r;
                        targetCol = c;
                        break;
                    }
                }
                if (targetRow != -1)
                    break;
            }

            assertTrue("Should find an empty seat to test booking", targetRow != -1);
            System.out.println("Found empty seat at: " + targetRow + "," + targetCol);

            Boolean booked = userBookingService.bookTrainSeat(train, targetRow, targetCol, "bangalore", "delhi",
                    "2026-07-21");
            System.out.println("BOOKED RESULT: " + booked);
            assertTrue("Seat booking should be successful", booked);

            List<Ticket> tickets = testUser.getTicketsBooked();
            assertFalse("Tickets list should not be empty after booking", tickets.isEmpty());

            Ticket ticket = tickets.get(0);
            assertEquals("Ticket destination should match booking", "delhi", ticket.getDestination());

            Boolean canceled = userBookingService.cancelBooking(ticket.getTicketId());
            assertTrue("Ticket cancellation should succeed", canceled);
            assertTrue("Tickets list should be empty after cancellation", testUser.getTicketsBooked().isEmpty());
        }
    }
}

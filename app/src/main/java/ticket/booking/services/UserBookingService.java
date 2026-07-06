package ticket.booking.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private static final String USER_PATH="app/src/main/java/ticket/booking/localDb/user.json";
    private List<User> userList;
    ObjectMapper mapper = new ObjectMapper();

        public UserBookingService(User user) throws IOException {
            this.user=user;
            loadUserList();
        }
        public UserBookingService() throws IOException {
            loadUserList();
        }
        public List<User> loadUserList() throws IOException {
            File users = new File(USER_PATH);
            return userList= mapper.readValue(users, new TypeReference<List<User>>() {});
        }
     public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }
    public Boolean singUp(User user1){
            try{
                userList.add(user1);
                saveUserListToFile();
                return Boolean.TRUE;
            }catch(IOException ex){
                return Boolean.FALSE;
            }
    }
    private void saveUserListToFile()throws IOException{
            File file = new File(USER_PATH);
            mapper.writeValue(file, userList);
    }
    public void fetchBooking(){
            user.printTicket();
    }
    public boolean cancelBooking(String TicketId){

            return Boolean.FALSE;
    }
}

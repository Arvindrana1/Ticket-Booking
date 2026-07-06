package ticket.booking.entities;

import java.util.List;

public class User {
    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userId;
    public User(String name, String password, String hashedPassword, List<Ticket> tickets, String userId) {
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = tickets;
        this.userId = userId;
    }
    public User(){}

    public void setName(String name){
        this.name=name;
    }
    public void setHashedPassword(String hashedPassword){
      this.hashedPassword=hashedPassword;
    }
    public void setTicketsBooked(List<Ticket> ticketsBooked){
        this.ticketsBooked=ticketsBooked;// it says to convert the ticketsBooked into string then put it into the list but the list is already giving me the user a a list
    }

    public void printTicket(){
        if(ticketsBooked==null) {
            for (int i = 0; i < ticketsBooked.size(); i++) {
                System.out.println(ticketsBooked.get(i).getTicketInfo());
            }
        }
    }
    public String getName() {
            return name;
    }
    public String getPassword() {
        return password;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }
    public String getUserId(){
        return userId;
    }
}

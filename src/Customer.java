
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements User, Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private boolean isActive;
    private List<String> orderedItems;
    private static final int MAX_ORDER_LIMIT = 10;

    public Customer(String firstName, String lastName, String email, String userName, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
        this.orderedItems = new ArrayList<>();
    }
	// xxx your codes
    
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public boolean isActive(){
        return isActive;
    }
    public List<String> getOrderedItems(){
        return orderedItems;
    }
    public String getRole(){
        return "Customer";
    }
    public void orderItems(MenuItem item) throws CustomExceptions.ItemNotAvailableException{
        //A menu item is available only if the remaining count > 0 and in-season is true
        if (item.isAvailable()){
            orderedItems.add(item.getTitle());
        }
        
    }
    public void setActive(boolean active){
        this.isActive = active;
    }
    public void setOrderedItems(List<String> orderedItems){
        this.orderedItems = orderedItems;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void cancelItem(MenuItem item){
        orderedItems.remove(item.getTitle());
    }
    public boolean canPlace(){
        return orderedItems.size() <= MAX_ORDER_LIMIT;
    }
    public String getDetails(){
        //I have no clue
        return "idk";
    }
    
    public String toDataString(){ // Convert the object to a string for saving to a file CHANGE LATER
        StringBuilder dataString = new StringBuilder();
    
        // Append each field to the string, separating with a delimiter (e.g., comma)
        dataString.append(firstName).append(",");
        dataString.append(lastName).append(",");
        dataString.append(email).append(",");
        dataString.append(userName).append(",");
        dataString.append(password).append(",");
        dataString.append(isActive).append(",");
        
        // For ordered items, join them into a single string, separated by semicolons
        dataString.append(String.join(";", orderedItems));
        
        return dataString.toString();
    }

    @Override
    public int compareTo(User otherCustomer) { //CHANGE LATER
        //Change to compare by some kind of variable
        if (otherCustomer == null) {
            throw new NullPointerException("Cannot compare to null");
        }

        // Compare first by firstName
        int firstNameComparison = this.firstName.compareTo(otherCustomer.getFirstName());
        
        // If firstName is the same, compare by lastName
        if (firstNameComparison == 0) {
            return this.lastName.compareTo(otherCustomer.getLastName());
        }
        
        return firstNameComparison;
}
}

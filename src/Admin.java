import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Admin implements User, Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private boolean isActive;
    private List<String> orderedItems;
    private static final int MAX_ORDER_LIMIT = 10;

    // Constructor to initialize an Admin object
    public Admin(String firstName, String lastName, String email, String userName, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
        this.orderedItems = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<String> getOrderedItems() {
        return orderedItems;
    }

    public String getRole() {
        return "Admin";
    }
    

    public void orderItems(MenuItem item) throws CustomExceptions.ItemNotAvailableException {
        // A menu item is available only if the remaining count > 0 and in-season is true
        if (item.isAvailable()) {
            orderedItems.add(item.getTitle());
        } else {
            throw new CustomExceptions.ItemNotAvailableException("Item is not available");
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setOrderedItems(List<String> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void cancelItem(MenuItem item) {
        orderedItems.remove(item.getTitle());
    }

    public boolean canPlace() {
        return orderedItems.size() <= MAX_ORDER_LIMIT;
    }

    public String getDetails() {
        return "Admin Details: " +
                "First Name: " + firstName +
                ", Last Name: " + lastName +
                ", Email: " + email +
                ", User Name: " + userName +
                ", Active: " + isActive +
                ", Ordered Items: " + orderedItems;
    }

    public String toDataString() { // Convert the object to a string for saving to a file
        StringBuilder dataString = new StringBuilder();

        // Append each field to the string, separating with a delimiter (e.g., comma)
        dataString.append("Admin").append(";");
        dataString.append(firstName).append(";");
        dataString.append(lastName).append(";");
        dataString.append(email).append(";");
        dataString.append(userName).append(";");
        dataString.append(password).append(";");
        dataString.append(isActive).append(";");

        // For ordered items, join them into a single string, separated by semicolons
        dataString.append(String.join(";", orderedItems));

        return dataString.toString();
    }

    @Override
    public int compareTo(User otherAdmin) {
        if (otherAdmin == null) {
            throw new NullPointerException("Cannot compare to null");
        }

        // Compare first by firstName
        int firstNameComparison = this.firstName.compareTo(otherAdmin.getFirstName());

        // If firstName is the same, compare by lastName
        if (firstNameComparison == 0) {
            return this.lastName.compareTo(otherAdmin.getLastName());
        }

        return firstNameComparison;
    }
    
}
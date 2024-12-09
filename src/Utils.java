import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.*;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

public class Utils {

    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static Border createEmptyBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    public static Color createTransparentColor(int r, int g, int b, int a) {
        return new Color(r, g, b, a);
    }

    public static void sortMenuItems(List<MenuItem> menuItems, String sortBy) {
        Collections.sort(menuItems, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem item1, MenuItem item2) {
                switch (sortBy) {
                    case "title":
                        return item1.getTitle().compareTo(item2.getTitle());
                    case "description":
                        return item1.getDescription().compareTo(item2.getDescription());
                    case "itemID":
                        return item1.getItemID().compareTo(item2.getItemID());
                    case "price":
                        return Float.compare(item1.getPrice(), item2.getPrice());
                    default:
                        return 0;
                }
            }
        });   
    }
    public static void sortUsers(List<User> users, String sortBy) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                switch (sortBy) {
                    case "firstName":
                        return user1.getFirstName().compareTo(user2.getFirstName());
                    case "lastName":
                        return user1.getLastName().compareTo(user2.getLastName());
                    case "email":
                        return user1.getEmail().compareTo(user2.getEmail());
                    case "userName":
                        return user1.getUserName().compareTo(user2.getUserName());
                    default:
                        return 0;
                }
            }
        });
    }
    public static List<MenuItem> searchMenuItems(List<MenuItem> menuItems, String query) {
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        return menuItems.stream()
                .filter(item -> {
                    Matcher matcher = pattern.matcher(item.getTitle());
                    return matcher.find();
                })
                .collect(Collectors.toList());
    }

    // Searching method for Users
    public static List<User> searchUsers(List<User> users, String query) {
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        return users.stream()
                .filter(user -> {
                    Matcher matcher = pattern.matcher(user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " " + user.getUserName());
                    return matcher.find();
                })
                .collect(Collectors.toList());
    }
    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }
        return true;
    }
}
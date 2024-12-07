import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum cafe {
    DB; // singleton design

    private List<MenuItem> menu;
    private Map<String, User> users;

    private cafe() { // must be private
        menu = new ArrayList<>();
        users = new HashMap<>();
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(String userName, User user) {
        users.put(userName, user);
    }

    public void removeUser(String userName) {
        users.remove(userName);
    }

    public void addMenuItem(MenuItem newItem) {
        menu.add(newItem);
    }

    public void removeMenuItem(MenuItem item) {
        menu.remove(item);
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cafeData.txt"))) {
            for (User user : users.values()) {
                writer.write(user.toDataString());
                writer.newLine();
            }
            for (MenuItem item : menu) {
                writer.write(item.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cafeData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals("Admin")) {
                    Admin admin = new Admin(parts[1], parts[2], parts[3], parts[4], parts[5], Boolean.parseBoolean(parts[6]));
                    admin.setOrderedItems(new ArrayList<>(List.of(parts[7].split(";"))));
                    users.put(parts[4], admin);
                } else if (parts[0].equals("Customer")) {
                    Customer customer = new Customer(parts[1], parts[2], parts[3], parts[4], parts[5], Boolean.parseBoolean(parts[6]));
                    customer.setOrderedItems(new ArrayList<>(List.of(parts[7].split(";"))));
                    users.put(parts[4], customer);
                } else if (parts[0].equals("Diner")) {
                    DinerMenuItem dinerItem = new DinerMenuItem(parts[1], parts[2], parts[3], Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                    menu.add(dinerItem);
                } else if (parts[0].equals("Pancake")) {
                    PancakeMenuItem pancakeItem = new PancakeMenuItem(parts[1], parts[2], parts[3], Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                    menu.add(pancakeItem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
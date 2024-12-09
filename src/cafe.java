import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum cafe {
    DB; // singleton design

    private List<MenuItem> menu;
    private Map<String, User> users;

    private cafe() { // must be private
        menu = new ArrayList<>();
        users = new HashMap<>();
        loadData();
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(String userName, User user) {
        users.put(userName, user);
        saveData();
    }

    public void removeUser(String userName) {
        users.remove(userName);
        saveData();
    }

    public void addMenuItem(MenuItem newItem) {
        menu.add(newItem);
        saveData();
    }

    public void removeMenuItem(MenuItem item) {
        menu.remove(item);
        saveData();
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
                System.out.println("Processing line: " + line);
                String[] parts = line.split(";");
                System.out.println("Parts array: " + Arrays.toString(parts));

                if (parts.length < 7) {
                    System.err.println("Invalid data format: " + line);
                    continue;
                }

                try {
                    switch (parts[0]) {
                        case "Admin":
                            Admin admin = new Admin(parts[1], parts[2], parts[3], parts[4], parts[5], Boolean.parseBoolean(parts[6]));
                            if (parts.length >= 8) {
                                admin.setOrderedItems(new ArrayList<>(List.of(parts[7].split(";"))));
                            } else {
                                admin.setOrderedItems(new ArrayList<>());
                            }
                            users.put(parts[4], admin);
                            break;
                        case "Customer":
                            Customer customer = new Customer(parts[1], parts[2], parts[3], parts[4], parts[5], Boolean.parseBoolean(parts[6]));
                            if (parts.length >= 8) {
                                customer.setOrderedItems(new ArrayList<>(List.of(parts[7].split(";"))));
                            } else {
                                customer.setOrderedItems(new ArrayList<>());
                            }
                            users.put(parts[4], customer);
                            break;
                        case "Diner":
                            DinerMenuItem dinerItem = new DinerMenuItem(parts[1], parts[2], parts[3], Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                            menu.add(dinerItem);
                            break;
                        case "Pancake":
                            PancakeMenuItem pancakeItem = new PancakeMenuItem(parts[1], parts[2], parts[3], Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                            menu.add(pancakeItem);
                            break;
                        default:
                            System.err.println("Unknown data type: " + parts[0]);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
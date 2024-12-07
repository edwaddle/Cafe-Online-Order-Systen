import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class PancakeMenuItem implements MenuItem, Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String itemID;
    private String description;
    private float price;
    private int count;
    private boolean available;
    private boolean current;

    private ArrayList<MenuItem> menuItems = new ArrayList<>(); // Store menu items

    public PancakeMenuItem(String title, String itemID, String description, float price, int count, boolean current) {
        this.title = title;
        this.itemID = itemID;
        this.description = description;
        this.price = price;
        this.count = count;
        this.current = current;
        this.available = count > 0 && current;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getItemID() {
        return itemID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
        this.available = count > 0 && current;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean isCurrent() {
        return current;
    }

    @Override
    public void setCurrent(boolean current) {
        this.current = current;
        this.available = count > 0 && current;
    }

    @Override
    public String toDataString() {
        return "Pancake;" + title + ";" + itemID + ";" + description + ";" + price + ";" + count + ";" + current;
    }

    @Override
    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    @Override
    public void removeItem(String itemID) {
        menuItems.removeIf(item -> item.getItemID().equals(itemID));
    }

    @Override
    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }

    @Override
    public String getMenuType() {
        return "Pancake";
    }

    @Override
    public int compareTo(MenuItem other) {
        return this.title.compareTo(other.getTitle());
    }
}
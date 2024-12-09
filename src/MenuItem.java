import java.io.Serializable;
import java.util.Iterator;

public interface MenuItem extends Comparable<MenuItem>, Serializable {
    String getTitle();
    void setTitle(String title);
    String getItemID();
    String getDescription();
    void setDescription(String description);
    float getPrice();
    void setPrice(float price);
    int getCount();
    void setCount(int count);

    boolean isAvailable();
    void setAvailable(boolean available);

    boolean isCurrent();
    void setCurrent(boolean current);

    String toDataString();
    void addItem(MenuItem item);
    void removeItem(String itemID);
    Iterator<MenuItem> createIterator();

    String getMenuType();
}
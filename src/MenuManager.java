import java.util.List;
import java.util.Iterator;

public class MenuManager {

    private List<MenuItem> menuItems;

    public MenuManager(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void addMenuItem(MenuItem item) throws CustomExceptions.ItemAlreadyExistsException {
        if (menuItems.contains(item)) {
            throw new CustomExceptions.ItemAlreadyExistsException("Item already exists in the menu");
        }
        menuItems.add(item);
    }

    public void removeMenuItem(MenuItem item) throws CustomExceptions.ItemNotFoundException {
        if (!menuItems.remove(item)) {
            throw new CustomExceptions.ItemNotFoundException("Item not found in the menu");
        }
    }

    public void updateMenuItem(MenuItem oldItem, MenuItem newItem) throws CustomExceptions.ItemNotFoundException {
        int index = menuItems.indexOf(oldItem);
        if (index == -1) {
            throw new CustomExceptions.ItemNotFoundException("Item not found in the menu");
        }
        menuItems.set(index, newItem);
    }

    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}
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

	// xxx your codes
	public List<MenuItem> getMenu(){
		return menu;
	}

	public Map<String, User> getUsers(){
		return users;
	}
	public void addUser(String string, User user){
		users.put(string, user);
	}
	public void removeUser(String string, User user){
		users.remove(string, user);
	}
	public void addMenuItem(MenuItem newItem){
		menu.add(newItem);
	}
	public void removeMenuItem(MenuItem item){
		menu.remove(menu.indexOf(item));
	}
	
}

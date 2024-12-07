import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class UserManager {

    public UserManager() {
    }

    public void addUser(Map<String, User> users, User user) throws CustomExceptions.UserAlreadyExistsException {
        if (users.containsKey(user.getUserName())) {
            throw new CustomExceptions.UserAlreadyExistsException("User already exists");
        }
        users.put(user.getUserName(), user);
    }

    public void removeUser(Map<String, User> users, String userName) throws CustomExceptions.UserNotFoundException {
        if (!users.containsKey(userName)) {
            throw new CustomExceptions.UserNotFoundException("User not found");
        }
        users.remove(userName);
    }

    public void updateUser(Map<String, User> users, String userName, User newUser) throws CustomExceptions.UserNotFoundException {
        if (!users.containsKey(userName)) {
            throw new CustomExceptions.UserNotFoundException("User not found");
        }
        users.put(userName, newUser);
    }

    public List<User> searchUsers(Map<String, User> users, String query) {
        return users.values().stream()
                .filter(user -> user.getFirstName().contains(query) || user.getLastName().contains(query) || user.getEmail().contains(query))
                .collect(Collectors.toList());
    }

    public List<User> sortUsers(Map<String, User> users, String sortBy) {
        return users.values().stream()
                .sorted((u1, u2) -> {
                    switch (sortBy) {
                        case "firstName":
                            return u1.getFirstName().compareTo(u2.getFirstName());
                        case "lastName":
                            return u1.getLastName().compareTo(u2.getLastName());
                        case "email":
                            return u1.getEmail().compareTo(u2.getEmail());
                        default:
                            return u1.getUserName().compareTo(u2.getUserName());
                    }
                })
                .collect(Collectors.toList());
    }
}
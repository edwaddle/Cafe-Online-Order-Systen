public class CustomExceptions {
    public static class ItemNotAvailableException extends Exception {
        public ItemNotAvailableException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class InactiveUserException extends Exception {
        public InactiveUserException(String message) {
            super(message);
        }
    }

    public static class ItemAlreadyExistsException extends Exception {
        public ItemAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidMenuItemException extends Exception {
        public InvalidMenuItemException(String message) {
            super(message);
        }
    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
    public static class BadLocationException extends Exception {
        public BadLocationException(String message) {
            super(message);
        }
    }
}
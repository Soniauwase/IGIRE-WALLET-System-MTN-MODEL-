package LAB2;

public class CustomException {
        public  static class InvalidAmountException extends RuntimeException {
            public InvalidAmountException(String message) { super(message); }
        }

        public static class DuplicateTransactionException extends RuntimeException {
            public DuplicateTransactionException(String message) { super(message); }
        }

        public static class InsufficientBalanceException extends RuntimeException {
            public InsufficientBalanceException(String message) { super(message); }
        }

        public static class AccountNotFoundException extends RuntimeException {
            public AccountNotFoundException(String message) { super(message); }
        }

        public static class DatabaseException extends RuntimeException {
            public DatabaseException(String message, Throwable cause) { super(message, cause); }
        }
    }


;

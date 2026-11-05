package LAB3;

import LAB1.Account;
import LAB1.Transaction;
import LAB2.AccountDao;
import LAB2.DatabaseConnection;
import LAB2.TransactionDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;


public class TransactionService {
        private final AccountDao accountDao = new AccountDao();
        private final TransactionDao transactionDao = new TransactionDao();

        public boolean deposit(String accountId, double amount, String referenceId) {
            if (amount <= 0) {
                System.out.println("Deposit amount must be greater than zero.");
                return false;
            }

            Connection connection = null;
            try {
                connection = DatabaseConnection.getConnection();
                connection.setAutoCommit(false);

                boolean isUnique = transactionDao.validateTransactionReference(referenceId);
                if (!isUnique) {
                    System.out.println("Duplicate transaction detected! Reference ID '" + referenceId + "' has already been processed.");
                    return false;
                }

                Account account = accountDao.findAccountById(accountId);
                if (account == null) {
                    System.out.println("Account not found with ID: " + accountId);
                    connection.rollback();
                    return false;
                }

                double newBalance = account.getBalance() + amount;
                account.setBalance(newBalance);
                accountDao.updateAccount(account);

                Transaction tx = new Transaction(
                        "TXN-" + UUID.randomUUID().toString().substring(0, 8),
                        referenceId,
                        amount,
                        "DEPOSIT",
                        LocalDateTime.now(),
                        accountId
                );

                transactionDao.saveTransaction(connection, tx);

                connection.commit();
                System.out.println("You successfull deposit this amount "+ amount +  "  New balance= " + newBalance);
                return true;

            } catch (SQLException e) {
                if (connection != null) {
                    try {
                        connection.rollback();
                        System.out.println("Transaction rolled back due to error.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("Database error during deposit: " + e.getMessage());
                return false;
            } finally {
                if (connection != null) {
                    try {
                        connection.setAutoCommit(true);
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public boolean withdraw(String accountId, double amount, String referenceId) {
            if (amount <= 0) {
                System.out.println("Withdrawal amount must be greater than zero.");
                return false;
            }

            Connection connection = null;
            try {
                connection = DatabaseConnection.getConnection();
                connection.setAutoCommit(false);

                boolean isUnique = transactionDao.validateTransactionReference(referenceId);
                if (!isUnique) {
                    System.out.println("Duplicate transaction detected! Reference ID '" + referenceId + "' has already been processed.");
                    return false;
                }

                Account account = accountDao.findAccountById(accountId);
                if (account == null) {
                    System.out.println("Account not found with ID: " + accountId);
                    connection.rollback();
                    return false;
                }

                if (account.getBalance() < amount) {
                    System.out.println("Insufficient balance. Current balance: " + account.getBalance());
                    connection.rollback();
                    return false;
                }

                double newBalance = account.getBalance() - amount;
                account.setBalance(newBalance);
                accountDao.updateAccount(account);

                Transaction transaction= new Transaction(
                        "TXN-" + UUID.randomUUID().toString().substring(0, 8),
                        referenceId,
                        amount,
                        "WITHDRAW",
                        LocalDateTime.now(),
                        accountId
                );

                transactionDao.saveTransaction(connection, transaction);

                connection.commit();
                System.out.println("You  successful withdraw this amount "+ amount+"  New balance: " + newBalance);
                return true;

            } catch (SQLException e) {
                if (connection != null) {
                    try {
                        connection.rollback();
                        System.out.println("Transaction rolled back due to error.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("Database error during withdrawal: " + e.getMessage());
                return false;
            } finally {
                if (connection != null) {
                    try {
                        connection.setAutoCommit(true);
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public boolean transferMoney(String sourceAccountId, String targetAccountId, double amount, String referenceId) {
            if (amount <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return false;
            }

            Connection connection = null;
            try {
                connection = DatabaseConnection.getConnection();
                connection.setAutoCommit(false);

                if (!transactionDao.validateTransactionReference(referenceId)) {
                    System.out.println("Duplicate request rejected. Reference ID '" + referenceId + "' was already processed.");
                    return false;
                }

                Account sourceAccount = accountDao.findAccountById(sourceAccountId);
                Account targetAccount = accountDao.findAccountById(targetAccountId);

                if (sourceAccount == null || targetAccount == null) {
                    System.out.println("Invalid source or target account ID.");
                    connection.rollback();
                    return false;
                }

                if (sourceAccount.getBalance() < amount) {
                    System.out.println("Insufficient balance in source account. Current balance: " + sourceAccount.getBalance());
                    connection.rollback();
                    return false;
                }

                sourceAccount.setBalance(sourceAccount.getBalance() - amount);
                targetAccount.setBalance(targetAccount.getBalance() + amount);

                accountDao.updateAccount(sourceAccount);
                accountDao.updateAccount(targetAccount);

                Transaction senderTx = new Transaction(
                        "TXN-OUT-" + UUID.randomUUID().toString().substring(0, 8),
                        referenceId,
                        amount,
                        "TRANSFER_OUT",
                        LocalDateTime.now(),
                        sourceAccountId
                );

                transactionDao.saveTransaction(connection, senderTx);

                connection.commit();
                System.out.println("Successfully transferred " + amount + " from " + sourceAccountId + " to " + targetAccountId);
                return true;

            } catch (SQLException e) {
                if (connection != null) {
                    try {
                        connection.rollback();
                        System.out.println("Transfer failed. Database rolled back successfully.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("Error during transfer: " + e.getMessage());
                return false;
            } finally {
                if (connection != null) {
                    try {
                        connection.setAutoCommit(true);
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


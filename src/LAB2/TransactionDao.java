package LAB2;

import LAB1.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao implements TransactionInterfaceDao {

    @Override
    public void createTransactionTable() throws SQLException {
        String createSql = """
                create table if not exists transactions (
                    transaction_id varchar(50) primary key,
                    account_id varchar(50) not null,
                    reference_id varchar(50) unique not null,
                    transaction_type varchar(50) not null,
                    amount double precision not null,
                    timestamp timestamp default current_timestamp,
                    foreign key (account_id) references account(account_id) on delete cascade
                )
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createSql);
            System.out.println("Transactions table created successfully!");
        }
    }

    @Override
    public Transaction saveTransaction(Connection connection, Transaction transaction) throws SQLException {
        String insertSql = """
                insert into transactions (transaction_id, account_id, reference_id, transaction_type, amount, timestamp)
                values (?, ?, ?, ?, ?, ?)
                """;
        boolean isExternalConnection = (connection != null);
        Connection conn = isExternalConnection ? connection : DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setString(1, transaction.getTransactionId());
            ps.setString(2, transaction.getAccountId());
            ps.setString(3, transaction.getReferenceId());
            ps.setString(4, transaction.getTransactionType());
            ps.setDouble(5, transaction.getAmount());
            ps.setTimestamp(6, Timestamp.valueOf(
                    transaction.getTimestamp() != null ? transaction.getTimestamp() : LocalDateTime.now()
            ));

            ps.executeUpdate();
        } finally {
            if (!isExternalConnection && conn != null) {
                conn.close();
            }
        }
        return transaction;
    }

    @Override
    public Transaction findTransactionById(String transaction_id) throws SQLException {
        String query = """
                select transaction_id, account_id, reference_id, transaction_type, amount, timestamp
                from transactions where transaction_id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, transaction_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaction tx = new Transaction();
                    tx.setTransactionId(rs.getString("transaction_id"));
                    tx.setAccountId(rs.getString("account_id"));
                    tx.setReferenceId(rs.getString("reference_id")); // Direct String assignment
                    tx.setTransactionType(rs.getString("transaction_type"));
                    tx.setAmount(rs.getDouble("amount"));
                    tx.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    return tx;
                }
            }
        }
        return null;
    }

    @Override
    public List<Transaction> displayAllTransaction() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "select transaction_id, account_id, reference_id, transaction_type, amount, timestamp from transactions";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setTransactionId(rs.getString("transaction_id"));
                tx.setAccountId(rs.getString("account_id"));
                tx.setReferenceId(rs.getString("reference_id")); // Direct String assignment
                tx.setTransactionType(rs.getString("transaction_type"));
                tx.setAmount(rs.getDouble("amount"));
                tx.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                transactions.add(tx);
            }
        }
        return transactions;
    }

    @Override
    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String updateSql = "update transactions set transaction_type = ?, amount = ? where transaction_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSql)) {
            ps.setString(1, transaction.getTransactionType());
            ps.setDouble(2, transaction.getAmount());
            ps.setString(3, transaction.getTransactionId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteTransaction(String transaction_id) throws SQLException {
        String deleteSql = "delete from transactions where transaction_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteSql)) {
            ps.setString(1, transaction_id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(String account_number) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = """
                select t.transaction_id, t.account_id, t.reference_id, t.transaction_type, t.amount, t.timestamp
                from transactions t
                join account a on t.account_id = a.account_id
                where a.account_number = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account_number);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction tx = new Transaction();
                    tx.setTransactionId(rs.getString("transaction_id"));
                    tx.setAccountId(rs.getString("account_id"));
                    tx.setReferenceId(rs.getString("reference_id"));
                    tx.setTransactionType(rs.getString("transaction_type"));
                    tx.setAmount(rs.getDouble("amount"));
                    tx.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    transactions.add(tx);
                }
            }
        }
        return transactions;
    }

    @Override
    public boolean validateTransactionReference(String reference_id) throws SQLException {
        String query = "select 1 from transactions where reference_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, reference_id);
            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }
        }
    }



    public void exportTransactionsToCSV(String accountNumber, String filePath) {
        String reportSql = "SELECT transaction_id, account_id, amount,transaction_type, timestamp FROM transactions WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(reportSql)) {

            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery();
                 FileWriter writer = new FileWriter(filePath)) {

                writer.append("Transaction ID,Account ID,Amount,transaction_type,Timestamp\n");

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    writer.append(rs.getString("transaction_id"))
                            .append(",")
                            .append(rs.getString("account_id"))
                            .append(",")
                            .append(String.valueOf(rs.getDouble("amount")))
                            .append(",")
                            .append(rs.getString("transaction_type"))
                            .append(",")
                            .append(rs.getString("timestamp"))
                            .append("\n");
                }

                if (hasData) {
                    System.out.println("CSV Export successful! Saved to: " + filePath);
                } else {
                    System.out.println("No transactions found to export for account: " + accountNumber);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error at exporting to CSV: " + e.getMessage());
        }
    }
    public void printDailySummary(String accountId) {
        String sql = "SELECT DATE(timestamp) as tx_date, COUNT(*) as total_count, SUM(amount) as total_amount " +
                "FROM transactions WHERE account_id = ? GROUP BY DATE(timestamp) ORDER BY tx_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n--- Daily Summary for Account: " + accountId + " ---");
                System.out.println(String.format("%-15s | %-15s | %-15s", "Date", "Tx Count", "Total Amount"));
                System.out.println("--------------------------------------------------");

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(String.format("%-15s | %-15d | %-15.2f",
                            rs.getDate("tx_date"),
                            rs.getInt("total_count"),
                            rs.getDouble("total_amount")
                    ));
                }
                if (!found) {
                    System.out.println("No daily summary data available.");
                }
                System.out.println("--------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error generating daily summary: " + e.getMessage());
        }
    }
    public void printCustomerStatement(String accountId, CustomerDao customerDao) {
        String accountSql = "SELECT customer_id, account_number, account_type, balance FROM account WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(accountSql)) {

            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No account found with ID: " + accountId);
                    return;
                }

                String customerId = rs.getString("customer_id");
                String accountNumber = rs.getString("account_number");
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");

                String customerName = "N/A";
                String customerEmail = "N/A";

                String custSql = "SELECT customer_name, email FROM customer WHERE customer_id = ?";
                try (PreparedStatement custStmt = conn.prepareStatement(custSql)) {
                    custStmt.setString(1, customerId);
                    try (ResultSet custRs = custStmt.executeQuery()) {
                        if (custRs.next()) {
                            customerName = custRs.getString("customer_name");
                            customerEmail = custRs.getString("email");
                        }
                    }
                }

                System.out.println("==========IGIRE PAYMENT SYSTEM STATEMENT==========");
                System.out.println("Customer Name  : " + customerName);
                System.out.println("Email          : " + customerEmail);
                System.out.println("Account ID     : " + accountId);
                System.out.println("Account Number : " + accountNumber);
                System.out.println("Account Type   : " + accountType);
                System.out.println("Current Balance: " + String.format("%.2f", balance));
                System.out.println("Statement Date : " + java.time.LocalDate.now());
                System.out.println(String.format("%-12s | %-12s | %-10s | %-20s", "ID", "Type", "Amount", "Date/Time"));
                List<Transaction> history = getTransactionHistory(accountId);
                if (history.isEmpty()) {
                    System.out.println("No activity found for this account.");
                } else {
                    for (Transaction t : history) {
                        System.out.println(String.format("%-12s | %-12s | %-10.2f | %-20s",
                                t.getTransactionId(),
                                t.getTransactionType(),
                                t.getAmount(),
                                t.getTimestamp()
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error printing statement: " + e.getMessage());
        }
    }
}
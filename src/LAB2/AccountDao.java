package LAB2;

import LAB1.Account;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



public class AccountDao implements AccountInterfaceDao {

        @Override
        public void createAccountTable() throws SQLException {
            String createSql = """
                create table account(
                    account_id varchar(50) primary key,
                    account_number varchar(50) unique not null,
                    account_type varchar(50) CHECK (account_type IN ('saving account', 'wallet account')),
                    balance DOUBLE PRECISION,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    customer_id varchar(50) not null,
                    foreign key (customer_id) references customer(customer_id) ON DELETE CASCADE
                )
                """;
            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute(createSql);
                System.out.println("Account table created successfully!");
            }
        }

        @Override
        public Account creatingAccount(Account account) throws SQLException {
            String insertSql = "insert into account(account_id, account_number, account_type, balance, created_at, customer_id) values (?, ?, ?, ?, ?, ?)";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, account.getAccountId());
                ps.setString(2, account.getAccountNumber());
                ps.setString(3, account.getAccountType());
                ps.setDouble(4, account.getBalance());
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(6, account.getCustomerId());

                ps.executeUpdate();
                account.setCreatedAt(LocalDateTime.now());
            }
            return account;
        }

        @Override
        public boolean updateAccount(Account account) throws SQLException {
            String updateSql = "update account set account_type = ?, balance = ? where account_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setString(1, account.getAccountType());
                ps.setDouble(2, account.getBalance());
                ps.setString(3, account.getAccountId());
                return ps.executeUpdate() > 0;
            }
        }

        @Override
        public List<Account> allAccount() throws SQLException {
            List<Account> accounts = new ArrayList<>();
            String query = "select account_id, account_number, account_type, balance, created_at, customer_id from account";

            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {
                while (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getString("account_id"));
                    account.setAccountNumber(rs.getString("account_number"));
                    account.setAccountType(rs.getString("account_type"));
                    account.setBalance(rs.getDouble("balance"));
                    account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    account.setCustomerId(rs.getString("customer_id"));
                    accounts.add(account);
                }
            }
            return accounts;
        }

        @Override
        public Account findAccountById(String account_id) throws SQLException {
            String findByIdSql = "select account_id, account_number, account_type, balance, created_at, customer_id from account where account_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(findByIdSql)) {
                ps.setString(1, account_id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Account account = new Account();
                        account.setAccountId(rs.getString("account_id"));
                        account.setAccountNumber(rs.getString("account_number"));
                        account.setAccountType(rs.getString("account_type"));
                        account.setBalance(rs.getDouble("balance"));
                        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        account.setCustomerId(rs.getString("customer_id"));
                        return account;
                    }
                }
            }
            return null;
        }

        @Override
        public boolean deleteAccount(String account_id) throws SQLException {
            String deleteAccountSql = "delete from account where account_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(deleteAccountSql)) {
                ps.setString(1, account_id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Account " + account_id + " deleted successfully.");
                    return true;
                }
            }
            return false;
        }
    }


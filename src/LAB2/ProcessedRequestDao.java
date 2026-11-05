package LAB2;

import java.sql.*;
import java.time.LocalDateTime;
import java.sql.*;


public class ProcessedRequestDao {
        private static final String URL = "jdbc:postgresql://localhost:5432/IgirepaymentSystem";
        private static final String USER = "postgres";
        private static final String PASSWORD = "Admin";

        public void createProcessedRequestTable() throws SQLException {
            String createSql = """
                create table processed_requests (
                    id serial primary key,
                    reference_id varchar(50) unique not null,
                    processed_at timestamp default current_timestamp
                )
                """;
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {
                statement.execute(createSql);
                System.out.println("Processed Requests table created successfully!");
            }
        }

        public boolean isRequestProcessed(String referenceId) throws SQLException {
            String query = "select 1 from processed_requests where reference_id = ?";
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, referenceId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }

        public void logProcessedRequest(String referenceId) throws SQLException {
            String insert = "insert into processed_requests (reference_id, processed_at) values (?, ?)";
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = connection.prepareStatement(insert)) {
                ps.setString(1, referenceId);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.executeUpdate();
            }
        }
    }


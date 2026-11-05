package LAB2;

import LAB1.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionInterfaceDao {
        void  createTransactionTable()throws SQLException;
        Transaction saveTransaction(Connection connection , Transaction transaction)throws SQLException;
        Transaction findTransactionById(String transaction_id)throws SQLException;
        List<Transaction> displayAllTransaction()throws SQLException;
        boolean updateTransaction(Transaction transaction)throws SQLException;
        boolean deleteTransaction(String transaction_id)throws SQLException;
        List<Transaction>getTransactionHistory(String account_number)throws SQLException;
        boolean validateTransactionReference(String reference_id)throws SQLException;


    }



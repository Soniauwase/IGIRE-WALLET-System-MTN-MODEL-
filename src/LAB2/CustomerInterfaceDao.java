package LAB2;

import LAB1.Customer;

import java.sql.SQLException;
import java.util.List;
import LAB1.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerInterfaceDao {
        void CreateTable()throws SQLException;
        Customer registerCustomer(Customer customer) throws SQLException;
        Customer findById(String id) throws SQLException;
        List<Customer> displayAll() throws SQLException;
        boolean updateCustomer(Customer customer) throws SQLException;
        void deleteCustomer(String customer_id) throws SQLException;
    }




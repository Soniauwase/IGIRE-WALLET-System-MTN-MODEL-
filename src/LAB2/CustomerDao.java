package LAB2;

import LAB1.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import LAB1.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao  implements CustomerInterfaceDao{
        @Override
        public void CreateTable() throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/IgirepaymentSystem", "postgres", "Admin");
            Statement statement= connection.createStatement();

            String CreateSql = """ 
                   create table customer(
        customer_Id varchar(50) primary key,
        customer_Name varchar(255) not null,
        email varchar(255) not null,
        phone_number varchar(50) not null,
        hash_pin varchar(255) not null
    )
""";
            statement.execute(CreateSql);
        }

        @Override
        public Customer registerCustomer(Customer customer) throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/IgirepaymentSystem", "postgres", "Admin");


            String insertSql= """
                insert into customer(customer_Id,customer_Name,email,phone_number,hash_pin) 
                values(?,?,?,?,?)
               """;
            PreparedStatement st= connection.prepareStatement(insertSql);
            st.setString(1,customer.getCustomerId());
            st.setString(2,customer.getCustomerName());
            st.setString(3,customer.getEmail());
            st.setString(4,customer.getPhoneNumber());
            st.setString(5, customer.getHash_pin());

            st.executeUpdate();


            return customer;

        }
        @Override
        public Customer findById(String customer_id) throws SQLException {
            String findCustomerById= """
                select customer_id, customer_name, email, phone_number from customer where customer_id=?;
                """;
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/IgirepaymentSystem", "postgres", "Admin");
            PreparedStatement preparedStatement=connection.prepareStatement(findCustomerById);
            preparedStatement.setString(1,customer_id);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                Customer customer= new Customer();
                customer.setCustomerId(resultSet.getString("customer_id"));
                customer.setCustomerName(resultSet.getString("customer_name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhoneNumber(resultSet.getString("phone_number"));
                return  customer;

            }


            return null;
        }
        public boolean updateCustomer(Customer customer) throws SQLException {
            String updateSql = """
            
                update customer
            set customer_Name = ?, email = ?, phone_number = ?
            where customer_id = ?
            """;

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/IgirepaymentSystem", "postgres", "Admin");
                 PreparedStatement pst = connection.prepareStatement(updateSql)) {

                pst.setString(1, customer.getCustomerName());
                pst.setString(2, customer.getEmail());
                pst.setString(3, customer.getPhoneNumber());
                pst.setString(4, customer.getCustomerId());

                int rowsUpdated = pst.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Customer " + customer.getCustomerId() + " updated successfully!");
                    return true;
                }
                else {

                    System.out.println("Customer " + customer.getCustomerId() + " is not found");
                    return  false;
                }
            }
        }

        @Override
        public List<Customer> displayAll() throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/IgirepaymentSystem", "postgres", "Admin");
            String displaySQL = "select * from customer";
            PreparedStatement preparedStatement = connection.prepareStatement(displaySQL);
            List<Customer>customers=new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getString("customer_id"));
                customer.setCustomerName(resultSet.getString("customer_name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhoneNumber(resultSet.getString("phone_number"));
                customers.add(customer);

            }
            return customers;
        }



        @Override
        public void deleteCustomer(String customer_id) throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/IgirepaymentSystem", "postgres", "Admin");
            String deleteSql= """
                delete  from customer where  customer_id=?;
                """;
            PreparedStatement preparedStatement= connection.prepareStatement(deleteSql);
            preparedStatement.setString(1,customer_id);
            preparedStatement.executeUpdate();

        }
    }



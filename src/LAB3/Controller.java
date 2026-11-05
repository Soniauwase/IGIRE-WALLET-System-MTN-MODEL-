package LAB3;

import LAB1.Account;
import LAB1.Customer;
import LAB1.Transaction;
import LAB2.AccountDao;
import LAB2.CustomerDao;
import LAB2.TransactionDao;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class Controller {
        static final Scanner scanner = new Scanner(System.in);
        static final CustomerDao customerDao = new CustomerDao();
        static final AccountDao accountDao = new AccountDao();
        TransactionService transactionService = new TransactionService();
        TransactionDao txDao = new TransactionDao();
        boolean condition = true;
        int choice;

        public void customerMenu() throws SQLException {
            while (true) {
                System.out.println("===== welcome to customer management system =========");
                System.out.println("1.register customer");
                System.out.println("2.update customer information");
                System.out.println("3. view customer account");
                System.out.println("4. exit");
                System.out.println(" enter your choice:");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        System.out.println("====== welcome to register customer =========");
                        System.out.println("enter customer ID: ");
                        String customer_id = scanner.next();
                        scanner.nextLine();
                        System.out.println("enter customer name:");
                        String customer_name = scanner.nextLine();
                        System.out.println("enter your email:");
                        String email = scanner.nextLine();
                        System.out.println("enter your phone number:");
                        String phone_number = scanner.nextLine();
                        System.out.println(" enter your  account PIN ");
                        String pin=scanner.nextLine();
                        if (!pin.matches("\\d{4}")) {
                            System.out.println("PIN must be exactly 4 digits.");
                            return;
                        }
                        String pinHash = hashPin(pin);
                        customerDao.registerCustomer(new Customer(customer_id, customer_name, email, phone_number,pinHash));
                        System.out.println("customer registered successfully!");

                    }
                    case 2 -> {
                        System.out.println("========= Update Customer ==========");
                        System.out.print("Enter Customer ID you want to update: ");
                        String customerId = scanner.next();
                        scanner.nextLine();
                        Customer existingCustomer = customerDao.findById(customerId);

                        if (existingCustomer == null) {
                            System.out.println("Customer with ID " + customerId + " does not exist!");
                            return;
                        }

                        System.out.println("--- existing Customer Details ---");
                        System.out.println("Name: " + existingCustomer.getCustomerName());
                        System.out.println("Email: " + existingCustomer.getEmail());
                        System.out.println("Phone: " + existingCustomer.getPhoneNumber());
                        System.out.println("--------------------------------");

                        System.out.print("Enter new customer name: ");
                        String newName = scanner.nextLine();

                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();

                        System.out.print("Enter new phone number: ");
                        String newPhone = scanner.nextLine();

                        existingCustomer.setCustomerName(newName);
                        existingCustomer.setEmail(newEmail);
                        existingCustomer.setPhoneNumber(newPhone);

                        customerDao.updateCustomer(existingCustomer);

                    }
                    case 3 -> {
                        System.out.println("===== welcome to viewing customer account details===========");
                        System.out.print("Enter Customer ID to view: ");
                        String customerId = scanner.next();
                        scanner.nextLine();

                        Customer customer = customerDao.findById(customerId);

                        if (customer == null) {
                            System.out.println(" Customer with ID " + customerId + " was not found!");
                        } else {
                            System.out.println("======== CUSTOMER DETAILS===========");
                            System.out.println("Customer ID  : " + customer.getCustomerId());
                            System.out.println("Full Name    : " + customer.getCustomerName());
                            System.out.println("Email        : " + customer.getEmail());
                            System.out.println("Phone Number : " + customer.getPhoneNumber());
                            System.out.println("PIN (Hashed) : " + customer.getHash_pin());
                        }
                    }

                    case 4 -> {
                        System.out.println(" thank you for using our system");
                        condition = false;

                        return;
                    }
                }
            }
        }
        public static String hashPin(String pin) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] bytes = md.digest(pin.getBytes("UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (byte b : bytes) sb.append(String.format("%02x", b));
                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void accountMenu() throws SQLException {
            while (true) {
                System.out.println("====== Welcome to Account Management===========");
                System.out.println("1. create wallet account");
                System.out.println("2. create  saving account");
                System.out.println("3.view account balance");
                System.out.println("4.delete inactive balance");
                System.out.println("5.Exit");

                System.out.println("enter your choice");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        System.out.println("====== welcome to create wallet account=========");
                        System.out.println("enter the account id:");
                        String account_id = scanner.next();
                        System.out.println("enter the account Number");
                        String account_number = scanner.next();
                        scanner.nextLine();
                        System.out.println("enter the account Type");
                        String account_type = scanner.nextLine();
                        System.out.println("enter your balance");
                        double balance = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("customer Id");
                        String customer_id = scanner.nextLine();
                        Account account = new Account(account_id, account_number, account_type, balance, LocalDateTime.now(), customer_id);
                        Account created = accountDao.creatingAccount(account);
                        if (created != null) {
                            System.out.println("  account:" + account.getAccountType() + "successfully created  for  this customer :" + customer_id);
                        }
                    }
                    case 2 -> {
                        System.out.println("====== welcome to create  Saving account=========");
                        System.out.println("enter the account id:");
                        String account_id = scanner.next();
                        System.out.println("enter the account Number");
                        String account_number = scanner.next();
                        scanner.nextLine();
                        System.out.println("enter the account Type");
                        String account_type = scanner.nextLine();
                        System.out.println("enter your balance");
                        double balance = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("customer Id");
                        String customer_id = scanner.nextLine();
                        Account account = new Account(account_id, account_number, account_type, balance, LocalDateTime.now(), customer_id);
                        Account created = accountDao.creatingAccount(account);
                        if (created != null) {
                            System.out.println("  account:" + account.getAccountType() + "successfully created  for  this customer :" + customer_id);
                        }


                    }
                    case 3 -> {
                        System.out.println("====== welcome to view account balance======");
                        System.out.println("enter account ID");
                        String account_id = scanner.next();
                        Account account = accountDao.findAccountById(account_id);
                        if (account != null) {
                            System.out.println(account.getAccountNumber());
                            System.out.println(account.getAccountType());
                            System.out.println(account.getBalance());
                        } else {
                            System.out.println("account with this account_id" + account_id + "is not found!");
                        }

                    }
                    case 4 -> {
                        System.out.println("===== welcome to  delete the inactive account");
                        System.out.println("enter  account id you want to delete");
                        String account_id = scanner.next();
                        boolean isDeleted = accountDao.deleteAccount(account_id);
                        if (isDeleted) {
                            System.out.println("account  with ID :" + account_id + "is deleted successfully!");
                        } else {
                            System.out.println("the deletion failed!"+ account_id +" you provide is not found!");
                        }
                    }
                    case 5 -> {
                        System.out.println("Thank you for using our system!");
                        return;

                    }
                    default -> {
                        System.out.println("you made the wrong choice , try again ! inter choice between 1-5 ");
                    }
                }
            }


        }

        ;

        public void transactionMenu() {
            while (true) {
                System.out.println("============= WELCOME TO TRANSACTION MANAGEMENT SYSTEM ==============");
                System.out.println("1. Deposit Money");
                System.out.println("2. Withdraw Money");
                System.out.println("3. Transfer Money");
                System.out.println("4. Transaction history");
                System.out.println("5. CSV transaction report");
                System.out.println("6. customer daily transaction report ");
                System.out.println("7. customer transaction statement");
                System.out.println("8.exit");
                System.out.print("Choose your option: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        System.out.print("Enter Account ID: ");
                        String depAccountId = scanner.nextLine();
                        System.out.print("Enter Amount to Deposit: ");
                        double depAmount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter Unique Reference ID (e.g., REF-001): ");
                        String depRefId = scanner.nextLine();

                        transactionService.deposit(depAccountId, depAmount, depRefId);
                        break;

                    case "2":
                        System.out.print("Enter Account ID: ");
                        String wthAccountId = scanner.nextLine();
                        System.out.print("Enter Amount to Withdraw: ");
                        double wthAmount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter Unique Reference ID (e.g., REF-002): ");
                        String wthRefId = scanner.nextLine();

                        transactionService.withdraw(wthAccountId, wthAmount, wthRefId);
                        break;

                    case "3":
                        System.out.print("Enter Source Account ID: ");
                        String srcId = scanner.nextLine();
                        System.out.print("Enter Target Account ID: ");
                        String tgtId = scanner.nextLine();
                        System.out.print("Enter Transfer Amount: ");
                        double trfAmount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter Unique Reference ID (e.g., REF-003): ");
                        String trfRefId = scanner.nextLine();

                        transactionService.transferMoney(srcId, tgtId, trfAmount, trfRefId);
                        break;

                    case "4":
                        System.out.println("======welcome view the transaction history========");
                        System.out.print("Enter Account Number to view history: ");
                        String accNum = scanner.next();
                        scanner.nextLine();
                        try {
                            List<Transaction> history = txDao.getTransactionHistory(accNum);
                            System.out.println("--- Transaction History for Account: " + accNum + " ---");
                            if (history.isEmpty()) {
                                System.out.println("No transactions found.");
                            } else {
                                for (Transaction transaction : history) {
                                    System.out.println(transaction);
                                }
                            }
                        } catch (SQLException e) {
                            System.out.println("Error fetching history: " + e.getMessage());
                        }
                        break;
                    case "5":
                        System.out.println("====== welcome to view transaction report history in CSV========");
                        System.out.println("enter the account number you want to check it's report:");
                        String accountNumber=scanner.next();
                        scanner.nextLine();
                        System.out.println(" enter the file name :");
                        String file_name=scanner.next();
                        scanner.nextLine();
                        txDao.exportTransactionsToCSV(accountNumber,file_name);
                        break;

                    case "6":
                        System.out.println(" ==== welcome to see daily transaction summary ======");
                        System.out.println(" enter the account number , you want to see daily summary");
                        String account_number=scanner.next();
                        scanner.nextLine();
                        txDao.printDailySummary(account_number);
                        break;

                    case "7":
                        System.out.println("===== View Customer Transaction Statement =====");
                        System.out.print("Enter Account Number for statement: ");
                        String accountNum = scanner.nextLine().trim();
                        txDao.printCustomerStatement(accountNum, customerDao);
                        break;
                    case "8":
                        System.out.println(" thank you for using our system");
                        condition=false;
                        return;
                    default:
                        System.out.println("Invalid transaction option.");
                }
                break;


            }
        }
    }












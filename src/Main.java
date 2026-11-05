import LAB3.Controller;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static final Scanner sc = new Scanner(System.in);

    static void main(String[] args) throws SQLException {
        boolean condition = true;
        while (true) {
            try {
                System.out.println("=============WELCOME TO IGIRE PAYMENT SYSTEM (IGPS) ===========");
                System.out.println("1.customer management system");
                System.out.println("2.Account management");
                System.out.println("3.Transaction management");
                System.out.println("4.exit");
                System.out.print("enter your choice:");
                int choice = sc.nextInt();


                while (true) {
                    switch (choice) {
                        case 1 -> {
                            Controller controller = new Controller();
                            controller.customerMenu();
                            return;
                        }
                        case 2 -> {
                            Controller controller = new Controller();
                            controller.accountMenu();
                            return;
                        }
                        case 3 -> {
                            Controller controller = new Controller();
                            controller.transactionMenu();
                            return;
                        }
                        case 4 -> {
                            System.out.println("THANK YOU FOR USING OUR SYSTEM");
                            condition = false;
                            return;
                        }
                        default -> {
                            System.out.println("wrong choice! choose between 1-4");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
};
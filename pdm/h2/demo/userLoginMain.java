package pdm.h2.demo;

import pdm.h2.demo.objects.DealerSale;
import pdm.h2.demo.objects.DealerVehicleInventory;

import javax.tools.JavaFileManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class userLoginMain {
    private static Scanner userin = new Scanner(System.in);

    public static void main(String[] args) {

        H2DemoMain demo = new H2DemoMain();
        String location = System.getProperty("user.dir") + "/test";
        String user = "scj";
        String password = "password";
        //Create the database connections, basically makes the database
        demo.createConnection(location, user, password);

        int choice;
        do {
            System.out.println("Select an option:\nLogin: 1\nCreate New User: 2");
            choice = userin.nextInt();

        }while ((choice != 1) && (choice != 2));

        if (choice == 1) {
            System.out.println("Enter in a email:");
            String email = userin.nextLine();
            System.out.println("Enter in a password:");
            String passwordLogin = userin.nextLine();

            String query = "SELECT * FROM customer WHERE Email=\'" + email + "\' AND Password=\'" + passwordLogin + "\';";

            //System.out.println(query);
            try {
                Statement stmt = demo.getConnection().createStatement();
                ResultSet result = stmt.executeQuery(query);

                // need .next !!
                result.next();
                System.out.printf("Customer %s: %s %s %d\n",
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4));
            } catch (SQLException e) {e.printStackTrace();}
        }

        else{
            System.out.println("Input SSN(Ex: 00-000-0000): ");
            String ssn = userin.next();
            ssn = ssn.substring(0,Math.min(11,ssn.length()));

            System.out.print("Input Your Full Name (max length 50): ");
            String name = userin.next();
            name = name.substring(0,Math.min(50,name.length()));

            System.out.print("Input Your Gender(max length 6): ");
            String gender = userin.next();
            gender = gender.substring(0,Math.min(6,gender.length()));

            System.out.print("Input Your income: ");
            int income = userin.nextInt();

            System.out.print("Input House Number: ");
            int houseNumber = userin.nextInt();

            System.out.print("Input Street (max length 50): ");
            String street = userin.next();
            street = street.substring(0,Math.min(50,street.length()));

            System.out.print("Input City (max length 20): ");
            String city = userin.next();
            city = city.substring(0,Math.min(20,city.length()));


            System.out.print("Input State (Ex. NY,WI,FL): ");
            String state = userin.next();
            state = state.toUpperCase();
            state = state.substring(0,Math.min(2,state.length()));

            System.out.print("Input Zipcode (Ex: 13021, 53066): ");
            String zipcode = userin.next();
            zipcode = zipcode.substring(0,Math.min(5,zipcode.length()));

            System.out.print("Input Email (max length 40): ");
            String email = userin.next();
            email = email.substring(0,Math.min(40,email.length()));

            System.out.print("Input Password (max length 20): ");
            String userPass = userin.next();
            userPass = userPass.substring(0,Math.min(20,userPass.length()));

                CustomerTable.addCustomer(demo.getConnection(),ssn,name,gender,income,houseNumber,street,city,state,zipcode,email,userPass);

            CustomerTable.printCustomerTable(demo.getConnection());
        }
    }
}

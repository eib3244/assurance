package pdm.h2.demo;

import pdm.h2.demo.objects.*;

import javax.tools.JavaFileManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Main Driver for user interaction (the "web ui" used to buy cars)
 */
public class userLoginMain {
    private static Scanner userin = new Scanner(System.in);

    public static void main(String[] args) {

        H2DemoMain demo = new H2DemoMain();
        String location = System.getProperty("user.dir") + "/test";
        String user = "scj";
        String password = "password";
        //Create the database connections, basically makes the database
        demo.createConnection(location, user, password);

        Customer currentCustomer = null;

        int choice;
        do {
            System.out.println("Select an option:\nLogin: 1\nCreate New User: 2");
            choice = userin.nextInt();

        }while ((choice != 1) && (choice != 2));

        // logging user in
        if (choice == 1) {
            // main loop we loop until a user is logged in.
            while (true) {
                System.out.print("Enter in a email:");
                String email = userin.next();
                System.out.print("Enter in a password:");
                String passwordLogin = userin.next();

                String query = "SELECT * FROM customer WHERE Email=\'" + email + "\' AND Password=\'" + passwordLogin + "\';";

                try {
                    // allows us to scroll forward and backwards in the result set so we can check how many people are in the result set.
                    Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result = stmt.executeQuery(query);
                    result.last();
                    int numOfPPL = result.getRow();
                    result.beforeFirst();

                    // no customer exists we go back to top of loop.
                    if (numOfPPL == 0){
                        System.out.println("No User in the database, please try again.");
                        continue;
                    }

                    // if one user exists we log them in and set them as the current customer
                    if (numOfPPL == 1) {
                        result.next();
                        String[] data = new String[result.getMetaData().getColumnCount()];

                        int i = 0;
                        while (i < result.getMetaData().getColumnCount()){
                            data[i] = result.getString(i+1);
                            i++;
                        }
                        currentCustomer = new Customer(data);
                        break;
                    }

                    // if two user share the same email and password we try to verify them via SSN
                    if (numOfPPL > 1) {

                        System.out.print("Enter your SSN for verification: ");
                        String ssn = userin.next();

                        boolean ssnFound = false;
                        while(result.next()){

                            // if we find the ssn we log the user in
                            if (ssn.equals(result.getString(1))){
                                String[] data = new String[result.getMetaData().getColumnCount()];

                                int i = 0;
                                while (i < result.getMetaData().getColumnCount()){
                                    data[i] = result.getString(i+1);
                                    i++;
                                }
                                currentCustomer = new Customer(data);
                                break;
                            }
                        }

                        // breaking out of loop is user logs in else we go back to the top of the loop
                        if (currentCustomer != null)
                            break;
                        else
                            System.out.println("User not found please try again.");
                    }

                } catch (SQLException e) {e.printStackTrace();}
            }
        }

        // creating new acct logic, getting info from user....
        else{
                System.out.println("Input SSN (Ex: 000-00-0000): ");
                String ssn = userin.next();
                ssn = ssn.substring(0, Math.min(11, ssn.length()));

                System.out.print("Input Your Full Name (max length 50): ");
                String name = userin.next();
                name = name.substring(0, Math.min(50, name.length()));

                System.out.print("Input Your Gender (max length 6): ");
                String gender = userin.next();
                gender = gender.substring(0, Math.min(6, gender.length()));

                System.out.print("Input Your income (Must be a whole number \ngreater than or equal to zero. Ex: 15000): ");
                int income = -1;
                // loop to ensure a number is inputted / no crash
                while(true) {
                    String incomeInput = userin.next();

                    try{
                        income = Integer.parseInt(incomeInput);
                    } catch (java.lang.NumberFormatException e){}
                    if (income >= 0)
                        break;
                    System.out.print("Please input a whole number \ngreater than or equal to 0 for your income: ");
                }
                
                System.out.print("Input House Number (Must be a whole number \ngreater than or equal to zero. Ex: 154): ");
                int houseNumber = -1;
                // loop to ensure a number is inputted / no crash
                while(true) {
                    String houseNumInput = userin.next();

                    try{
                        houseNumber = Integer.parseInt(houseNumInput);
                    } catch (java.lang.NumberFormatException e){}

                    if (houseNumber >= 0)
                    break;
                    System.out.print("Please input a whole number \ngreater than or equal to 0 for your House Number: ");
                }

                System.out.print("Input Street (max length 50): ");
                String street = userin.next();
                street = street.substring(0, Math.min(50, street.length()));

                System.out.print("Input City (max length 20): ");
                String city = userin.next();
                city = city.substring(0, Math.min(20, city.length()));


                System.out.print("Input State (Ex. NY,WI,FL): ");
                String state = userin.next();
                state = state.toUpperCase();
                state = state.substring(0, Math.min(2, state.length()));

                System.out.print("Input Zip-Code (Ex: 13021, 53066): ");
                String zipcode = userin.next();
                zipcode = zipcode.substring(0, Math.min(5, zipcode.length()));


                System.out.print("Enter Phone Numbers (Ex: 315-111-1111,315-222-3333)\nSeparate Each one by a comma: ");
                String phoneNumbers;
                phoneNumbers = userin.next();
                String[] phoneNumArray = phoneNumbers.split(",");
                int i = 0;
                while(i < phoneNumArray.length){
                    phoneNumArray[i] = phoneNumArray[i].substring(0, Math.min(12, phoneNumArray[i].length()));
                    i++;
                }

                System.out.print("Input Email (max length 40): ");
                String email = userin.next();
                email = email.substring(0, Math.min(40, email.length()));

                System.out.print("Input Password (max length 20): ");
                String userPass = userin.next();
                userPass = userPass.substring(0, Math.min(20, userPass.length()));

                // we check to see if the customer's SSN is in the Database yet.
                // we loop until they add a ssn that isn't in the database.
                while (true) {
                    boolean result = CustomerTable.checkIfInTable(demo.getConnection(), ssn);
                    if (result == false) {
                        System.out.println("User Created !");
                        CustomerTable.addCustomer(demo.getConnection(), ssn, name, gender, income, houseNumber, street, city, state, zipcode, email, userPass);
                        currentCustomer = new Customer(ssn,name,gender,income,houseNumber,street,city,state,zipcode,email,password);
                        break;
                    } else {
                        System.out.println("Error: SSN is already in use");
                        System.out.println("Input SSN (Ex: 000-00-0000): ");
                        ssn = userin.next();
                        ssn = ssn.substring(0, Math.min(11, ssn.length()));
                    }
                }
        }

        System.out.println("Hello: " + currentCustomer.getName());
        System.out.println(currentCustomer.getSSN());
    }
}

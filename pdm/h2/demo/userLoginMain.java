package pdm.h2.demo;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.Select;
import pdm.h2.demo.objects.*;

import javax.jws.soap.SOAPBinding;
import javax.tools.JavaFileManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.WeakHashMap;

/*
 * Main Driver for user interaction (the "web ui" used to buy cars)
 */
public class userLoginMain {

    private static Scanner userin = new Scanner(System.in);

    /*
     * Main method to log in a user
     */
    private static Customer loginCustomer(H2DemoMain demo){
        int choice;
        Customer currentCustomer = null;

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
                    currentCustomer = new Customer(ssn,name,gender,income,houseNumber,street,city,state,zipcode,email,userPass);
                    break;
                } else {
                    System.out.println("Error: SSN is already in use");
                    System.out.println("Input SSN (Ex: 000-00-0000): ");
                    ssn = userin.next();
                    ssn = ssn.substring(0, Math.min(11, ssn.length()));
                }
            }
        }
        return currentCustomer;
    }

    /*
     * Main loop for customer interaction
     */
    private static void customerInteractionLoop(H2DemoMain demo, Customer currentCustomer) {
        int option;
        System.out.println("Hello: " + currentCustomer.getName());

        option = -1;
        while (option != 3) {
            System.out.println("\n--Main Menu--");
            System.out.println("1: View Past sales");
            System.out.println("2: Search for a vehicle");
            System.out.println("3: Log out");
            System.out.print("Select an option: ");

            // loop to ensure a number is inputted / no crash
            while(true) {
                String optionInput = userin.next();

                try{
                    option = Integer.parseInt(optionInput);
                } catch (java.lang.NumberFormatException e){}
                if ((option > 0) && (option <= 3))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (option) {
                case 1:
                    System.out.println("\n--PAST SALES--");
                    viewPastSales(demo, currentCustomer);
                    break;

                case 2:
                    System.out.println("\n--VEHICLE SEARCH--");
                    viewVehicles(demo,currentCustomer);
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    break;
            }
        }
    }

    /*
     * Main Method to view past sales
     */
    private static void viewPastSales(H2DemoMain demo, Customer currentCustomer){

        try {
            String query = "SELECT * FROM customer "
                        + "INNER JOIN customer_sale_table ON customer.SSN = customer_sale_table.SSN "
                        + "INNER JOIN vehicles_sold_to_customer ON vehicles_sold_to_customer.Sale_ID = customer_sale_table.Sale_ID "
                        + "INNER JOIN vehicles ON vehicles.VIN = vehicles_sold_to_customer.VIN "
                        + "INNER JOIN dealer ON dealer.Dealer_ID = customer_sale_table.Dealer_ID "
                        + "WHERE customer.SSN=\'" + currentCustomer.getSSN() + "\';";

                Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet result = stmt.executeQuery(query);

                while (result.next()){
                    Date date = new Date(result.getLong(15));
                    System.out.println("-----");
                    System.out.println("Sale Date: " + date);
                    System.out.println("Sale ID: " +result.getString(12) );
                    System.out.println("Total Sale Price: " + result.getString(16));
                    System.out.println("Dealer: " + result.getString(31));
                    System.out.println("Car Info: ");
                    System.out.println(result.getString(23) + " "
                            + result.getString(25) + " "
                            +result.getString(20) + " "
                            +result.getString(21) + " "
                            +result.getString(24) + " "
                            +result.getString(27) + " "
                            +result.getString(29) + " miles");
                    System.out.println("-----");
            }
            result.last();
            int lastResult = result.getRow();
            if (lastResult == 0){
                System.out.println("You have not purchased a vehicle yet.");
            }

        }catch (SQLException e){e.printStackTrace();}
    }

    /*
     * Main Driver for viewing vehicles and buying them
     */
    private static void viewVehicles(H2DemoMain demo, Customer currentCustomer) {
        ArrayList<Dealer> dealers = new ArrayList<Dealer>();
        int numberOfDealers = 0;
        int dealerSelected = -1;

        try {
            String queryDealers = "SELECT * FROM dealer;";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queryDealers);

            while (result.next()) {
                dealers.add(new Dealer(result.getString(1), result.getString(2),
                        result.getInt(3), result.getString(4), result.getString(5),
                        result.getString(6), result.getString(7), result.getString(8)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < dealers.size(); i++) {
            System.out.println(i + ": " + dealers.get(i).getName());
            System.out.println(dealers.get(i).getStreet_Number() + " " + dealers.get(i).getStreet() + " " +
                    dealers.get(i).getCity() + " " + dealers.get(i).getState() + " " + dealers.get(i).getZip());
            System.out.println("--------");
            numberOfDealers++;
        }

        System.out.print("Select a Dealer to buy from (input their number): ");
        while (true) {
            String optionInput = userin.next();

            try {
                dealerSelected = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e) {
            }
            if ((dealerSelected >= 0) && (dealerSelected < numberOfDealers))
                break;
            System.out.print("Please input a number from the list above: ");
        }

        System.out.println(dealers.get(dealerSelected).getName() + " was selected");

        boolean stayInLoop = true;
        ArrayList <String> whereClauseArrayList = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            whereClauseArrayList.add("");
        }

        while (stayInLoop) {
            int selection = -1;
            System.out.println("\n--" + dealers.get(dealerSelected).getName() + "--");
            System.out.println("1: View vehicles");
            System.out.println("2: Alter search options");
            System.out.println("3: View cart");
            System.out.println("4: Main menu");

            ArrayList<Vehicle> cart = new ArrayList<Vehicle>();
            while (true) {
                String optionInput = userin.next();
                try {
                    selection = Integer.parseInt(optionInput);
                } catch (java.lang.NumberFormatException e) {
                }
                if ((selection > 0) && (selection <= 4))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (selection) {

                case 1:
                    cart = viewVehiclesToBuy(demo, whereClauseArrayList, dealers.get(dealerSelected).getDealer_ID(), cart);
                    break;

                case 2:
                    whereClauseArrayList =
                            alterWhereClause(demo, whereClauseArrayList, dealers.get(dealerSelected).getDealer_ID());
                    break;

                case 3:
                    break;

                case 4:
                    stayInLoop = false;
                    break;
            }
        }
    }

    private static ArrayList<Vehicle> viewVehiclesToBuy(H2DemoMain demo, ArrayList<String> whereClauseArrayList, String currentDealerID, ArrayList<Vehicle> cart){

        try {
            String query = "SELECT * FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN"
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\'";

            boolean addWhereClause = false;
            for (int i = 0; i < whereClauseArrayList.size(); i++){
                if (!whereClauseArrayList.get(i).equals("")){
                    addWhereClause = true;
                    break;
                }
            }

            if (addWhereClause){
                query = query + " AND ";
                if (!whereClauseArrayList.get(0).equals(""))
                    query = query + "vehicles.Make=\'" + whereClauseArrayList.get(0) +"\' AND ";

                if (!whereClauseArrayList.get(1).equals(""))
                    query = query + "vehicles.Model=\'" + whereClauseArrayList.get(1) +"\' AND ";

                if (!whereClauseArrayList.get(2).equals(""))
                    query = query + "vehicles.Brand=\'" + whereClauseArrayList.get(2) +"\' AND ";

                if (!whereClauseArrayList.get(3).equals(""))
                    query = query + "vehicles.Year=\'" + whereClauseArrayList.get(3) +"\' AND ";

                if (!whereClauseArrayList.get(4).equals(""))
                    query = query + "vehicles.Engine=\'" + whereClauseArrayList.get(4) +"\' AND ";

                if (!whereClauseArrayList.get(5).equals(""))
                    query = query + "vehicles.Color=\'" + whereClauseArrayList.get(5) +"\' AND ";

                if (!whereClauseArrayList.get(6).equals(""))
                    query = query + "vehicles.Transmission=\'" + whereClauseArrayList.get(6) +"\' AND ";

                if (!whereClauseArrayList.get(7).equals(""))
                    query = query + "vehicles.Drive_Type=\'" + whereClauseArrayList.get(7) +"\' AND ";

                // TODO price and miles need to be added !

            }

        System.out.println(("\'" + query.substring(query.length()-5, query.length()) + "\'"));

            System.out.println(query);
            if(query.substring(query.length()-5, query.length()).equals(" AND ")){
                query = query.substring(0,query.length()-5);
            }
            query = query + ";";

            System.out.println(query);
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);

            int i = 1;
            while (result.next()){
                System.out.println(i + ": " + result.getString(7) + " "
                        + result.getString(5) + " "
                        +result.getString(2) + " "
                        +result.getString(3) + " "
                        +result.getString(6) + " "
                        +result.getString(8) + " "
                        +result.getString(9) + " $"
                        +result.getString(10));
                System.out.println("-----");
                i++;
            }



        } catch (java.sql.SQLException e){}





        return cart;
    }

    /*
     * Used to alter the where clause for our query
     */
    private static ArrayList<String> alterWhereClause(H2DemoMain demo, ArrayList<String> whereClauseArrayList, String currentDealerID){
        String Make = whereClauseArrayList.get(0);
        String Model = whereClauseArrayList.get(1);
        String Brand = whereClauseArrayList.get(2);
        String Year = whereClauseArrayList.get(3);
        String Engine = whereClauseArrayList.get(4);
        String Color = whereClauseArrayList.get(5);
        String Transmission = whereClauseArrayList.get(6);
        String Drive_Type = whereClauseArrayList.get(7);
        String Price = whereClauseArrayList.get(8);
        String Miles =  whereClauseArrayList.get(9);

        boolean keepAltering = true;
        while (keepAltering) {
            System.out.println("\n--Search Options To Alter--");
            System.out.println("1:  Make Currently(" + Make + ")");
            System.out.println("2:  Model Currently(" + Model + ")");
            System.out.println("3:  Brand Currently(" + Brand + ")");
            System.out.println("4:  Year Currently(" + Year + ")");
            System.out.println("5:  Engine Currently(" + Engine + ")");
            System.out.println("6:  Color Currently(" + Color + ")");
            System.out.println("7:  Transmission Currently(" + Transmission + ")");
            System.out.println("8:  Drive_Type Currently(" + Drive_Type + ")");
            System.out.println("9:  Price Currently(" + Price + ")");
            System.out.println("10: Miles Currently(" + Miles + ")");
            System.out.println("11: Stop altering attributes");
            System.out.println("Note () means no preference.");
            System.out.print("Select an option: ");
            int selection = -1;
            while (true) {
                String optionInput = userin.next();
                try {
                    selection = Integer.parseInt(optionInput);
                } catch (java.lang.NumberFormatException e) {
                }
                if ((selection > 0) && (selection <= 11))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (selection) {
                case 1:
                    Make = alterVehicleAttOption(demo, "Make",selection + 1, currentDealerID);
                    break;

                case 2:
                    Model = alterVehicleAttOption(demo, "Model",selection + 1, currentDealerID);
                    break;

                case 3:
                    Brand = alterVehicleAttOption(demo, "Brand",selection + 1, currentDealerID);
                    break;

                case 4:
                    Year = alterVehicleAttOption(demo, "Year",selection + 1, currentDealerID);
                    break;

                case 5:
                    Engine = alterVehicleAttOption(demo, "Engine",selection + 1, currentDealerID);
                    break;

                case 6:
                    Color = alterVehicleAttOption(demo, "Color",selection + 1, currentDealerID);
                    break;

                case 7:
                    Transmission = alterVehicleAttOption(demo, "Transmission",selection + 1, currentDealerID);
                    break;

                case 8:
                    Drive_Type = alterVehicleAttOption(demo, "Drive_Type",selection + 1, currentDealerID);
                    break;

//TODO miles and price below need to be done do <, > than a # for these
                case 9:
                    break;

                case 10:
                    break;

                case 11:
                    keepAltering = false;
                    break;
            }
        }

        whereClauseArrayList.set(0,Make);
        whereClauseArrayList.set(1,Model);
        whereClauseArrayList.set(2,Brand);
        whereClauseArrayList.set(3,Year);
        whereClauseArrayList.set(4,Engine);
        whereClauseArrayList.set(5,Color);
        whereClauseArrayList.set(6,Transmission);
        whereClauseArrayList.set(7,Drive_Type);
        whereClauseArrayList.set(8,Price);
        whereClauseArrayList.set(9,Miles);

        return whereClauseArrayList;
    }

    /*
     * Helper fn created to alter strings from our alterWhereClause function.
     * This is used for everything except for price and miles
     */
    private static String alterVehicleAttOption(H2DemoMain demo, String optionToChange, int thingToChangeColumn, String currentDealerID){
        ArrayList<String > optionsToChooseFrom = new ArrayList<String>();

        try {
            String query = "SELECT * FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN "
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\';";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                if (!optionsToChooseFrom.contains(result.getString(thingToChangeColumn))){
                    optionsToChooseFrom.add(result.getString(thingToChangeColumn));
                }
            }

        } catch (java.sql.SQLException e){}

        System.out.println("\n--" + optionToChange + " To Choose From--");
        int i;
        for(i = 0; i < optionsToChooseFrom.size(); i++){
            System.out.println(i + ": " + optionsToChooseFrom.get(i));
        }
        System.out.println(i + ": Clear preference");

        int userSelection = -1;
        System.out.print("Select a " + optionToChange + ": ");
        while (true) {
            String optionInput = userin.next();
            try {
                userSelection = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e) {
            }
            if ((userSelection >= 0) && (userSelection < optionsToChooseFrom.size() + 1))
                break;
            System.out.print("Please input a number from the list above: ");
        }
        if(userSelection == optionsToChooseFrom.size()) {
            System.out.println("No preference selected for " + optionToChange + ".");
            return "";
        }
        else {
            System.out.println("You selected " + optionsToChooseFrom.get(userSelection) + " for your " + optionToChange + ".");
            return optionsToChooseFrom.get(userSelection);
        }
    }




    public static void main(String[] args) {

        H2DemoMain demo = new H2DemoMain();
        String location = System.getProperty("user.dir") + "/test";
        String user = "scj";
        String password = "password";
        //Create the database connections
        demo.createConnection(location, user, password);

        Customer currentCustomer = loginCustomer(demo);

        customerInteractionLoop(demo,currentCustomer);
    }
}

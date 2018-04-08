package pdm.h2.demo;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.Select;
import com.sun.org.apache.xpath.internal.operations.Mod;
import pdm.h2.demo.objects.*;

import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.tools.JavaFileManager;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * Main Driver for user interaction (the "web ui" used to buy cars)
 * TEST - Andrew 4-2-18
 */
public class userLoginMain {

    private static Scanner userin = new Scanner(System.in);

    /*
     * Main method to log in a user
     */
    private static Customer loginCustomer(H2DemoMain demo){

        Customer currentCustomer = null;
        int choice = -1;
        System.out.println("\n-----Customer Home-----");
        System.out.print("Login: 1\nCreate New User: 2\nSelect an option: ");
        while(true) {
          String userinput= userin.next();

          try{
              choice = Integer.parseInt(userinput);
          } catch (java.lang.NumberFormatException e){}

          if (choice != 1 && choice != 2){
              System.out.print("\nPlease enter a number from the list above: ");
          }
          else
              break;
        }

        // logging user in
        if (choice == 1) {
            System.out.println("\n-----Customer Login-----");

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

                        System.out.print("Enter your SSN for verification\n(Ex: 000-00-0000): ");
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
            System.out.println("\n-----New Customer Creation-----");

            System.out.print("Input SSN (Ex: 000-00-0000): ");
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
        System.out.println("\nHello: " + currentCustomer.getName());

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
                    System.out.println("\nGoodbye!");
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
            String queryDealers = "SELECT * FROM dealer ORDER BY(dealer.State);";
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
        ArrayList<Vehicle> cart = new ArrayList<Vehicle>();

        while (stayInLoop) {
            int selection = -1;
            System.out.println("\n--" + dealers.get(dealerSelected).getName() + "--");
            System.out.println("1: View vehicles");
            System.out.println("2: Alter search options");
            System.out.println("3: View cart (" + cart.size() + " cars in cart)");
            System.out.println("4: Main menu");
            System.out.print("Select an option: ");


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
                    System.out.println("\n-----Vehicles to Buy-----");
                    cart = viewVehiclesToBuy(demo, whereClauseArrayList, dealers.get(dealerSelected).getDealer_ID(), cart);
                    break;

                case 2:
                    whereClauseArrayList =
                            alterWhereClause(demo, whereClauseArrayList, dealers.get(dealerSelected).getDealer_ID());
                    break;

                case 3:
                    cart = viewCart(demo,cart,currentCustomer,dealers.get(dealerSelected).getDealer_ID());
                    break;

                case 4:
                    stayInLoop = false;
                    break;
            }
        }
    }

    private static ArrayList<Vehicle> viewVehiclesToBuy(H2DemoMain demo, ArrayList<String> whereClauseArrayList, String currentDealerID, ArrayList<Vehicle> cart){
        ResultSet result;
        ArrayList<Vehicle> vehiclesToSelect = new ArrayList<Vehicle>();
        int i = 1;

        try {
            String query = "SELECT * FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN"
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\'";

            boolean addWhereClause = false;
            for (int x = 0; x < whereClauseArrayList.size(); x++){
                if (!whereClauseArrayList.get(x).equals("")){
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

                if (!whereClauseArrayList.get(8).equals(""))
                    query = query + "vehicles.Price" + whereClauseArrayList.get(8) + " AND ";

                if (!whereClauseArrayList.get(9).equals(""))
                    query = query + "vehicles.Price" + whereClauseArrayList.get(9) + " AND ";
            }

            if(query.substring(query.length()-5, query.length()).equals(" AND ")){
                query = query.substring(0,query.length()-5);
            }
            query = query + " ORDER BY vehicles.Make asc;";

            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeQuery(query);
//TODO make all prints that involeve data that works with colums like this !!!!
            System.out.printf("%2s: %20s %10s %20s %30s %15s %15s %15s %15s %15s\n",
                    "--","Color","Year","Make","Model","Engine","Transmission","Drive Type","Price","Miles");

            System.out.println("----------------------------------------------------------------------------" +
                            "-------------------------------------------------" +
                            "----------------------------------------------");



            while (result.next()){
                Vehicle newVehicle = new Vehicle(result.getString(1), result.getString(2),
                        result.getString(3), result.getString(4),
                        Integer.parseInt(result.getString(5)),result.getString(6),
                        result.getString(7),result.getString(8),result.getString(9),
                        Integer.parseInt(result.getString(10)),Integer.parseInt(result.getString(11)));

                boolean inCart = false;
                for (int y = 0; y < cart.size(); y++) {
                    if (cart.get(y).getVIN() == newVehicle.getVIN()) {
                       inCart = true;
                    }
                }

                if (inCart)
                    continue;

                vehiclesToSelect.add(newVehicle);

                System.out.printf("%2d: %20s %10s %20s %30s %15s %15s %15s %15s %15s Mi.",i,
                        result.getString(7),
                        result.getString(5),
                        result.getString(2),
                        result.getString(3),
                        result.getString(6),
                        result.getString(8),
                        result.getString(9),
                        "$" + result.getString(10),
                        result.getString(11));
                System.out.println("\n----------------------------------------------------------------------------" +
                        "-------------------------------------------------" +
                        "----------------------------------------------");
                i++;
            }

            if (i == 1)
                System.out.println("\nNo vehicles found please alter search options");
            else {
                System.out.println(i + ": Return to prev menu");
                System.out.println("------------------------");
                System.out.print("Please select an option from the list above: ");

                int option = -1;
                // loop to ensure a number is inputted / no crash
                while(true) {
                    String optionInput = userin.next();

                    try{
                        option = Integer.parseInt(optionInput);
                    } catch (java.lang.NumberFormatException e){}
                    if ((option >= 1) && (option <= i))
                        break;
                    System.out.print("Please input a number from the list above: ");
                }
                if (option != i){
                    viewVehicleInDetail(demo, vehiclesToSelect.get(option-1), cart);
                }
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

            String modelsCurrentMake = "";
            if (!Model.equals("")){
                try {
                    String query = "SELECT * FROM vehicles "
                            + " WHERE Vehicles.Model =\'"
                            + Model + "\'";
                    Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result = stmt.executeQuery(query);
                    result.next();

                    modelsCurrentMake = result.getString(2);
                } catch (java.sql.SQLException e){}
            }

            String makesCurrentBrand = "";
            if (!Make.equals("")){
                try {
                    String query = "SELECT * FROM vehicles "
                            + " WHERE Vehicles.Make =\'"
                            + Make + "\'";
                    Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result = stmt.executeQuery(query);
                    result.next();

                    makesCurrentBrand = result.getString(4);
                } catch (java.sql.SQLException e){}
            }

            System.out.println("\n--Search Options To Alter--");

            if (makesCurrentBrand.equals(""))
                System.out.println("1:  Make Currently (" + Make + ")");
            else
                System.out.println("1:  Make Currently (" + Make + ") made by " + makesCurrentBrand);

            if (modelsCurrentMake.equals(""))
                System.out.println("2:  Model Currently (" + Model + ")");
            else
                System.out.println("2:  Model Currently (" + Model + ") made by " + modelsCurrentMake);

            System.out.println("3:  Brand Currently (" + Brand + ")");
            System.out.println("4:  Year Currently (" + Year + ")");
            System.out.println("5:  Engine Currently (" + Engine + ")");
            System.out.println("6:  Color Currently (" + Color + ")");
            System.out.println("7:  Transmission Currently (" + Transmission + ")");
            System.out.println("8:  Drive_Type Currently (" + Drive_Type + ")");
            System.out.println("9:  Price Currently (" + Price + ")");
            System.out.println("10: Miles Currently (" + Miles + ")");
            System.out.println("11: Stop altering attributes");
            System.out.println("Note () means no preference.");

            if (
                    (!Model.equals("")) && (!Make.equals("")) && (!Make.equals(modelsCurrentMake))
                    ||
                    (!Make.equals("")) && (!Brand.equals("")) && (!Brand.equals(makesCurrentBrand))) {
                System.out.println("*****\nNote! Your search will currently return 0 vehicles as\n"
                        + "your selected \'Make\' must be made by your selected \'Brand\' and \n" +
                        "your selected  \'Model\' must be made by your selected \'Make\'\n*****");
            }
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
                    Make = alterVehicleAttOption(demo, "Make",selection + 1, currentDealerID, Make);
                    break;

                case 2:
                    Model = alterVehicleAttOption(demo, "Model",selection + 1, currentDealerID, Make);
                    break;

                case 3:
                    Brand = alterVehicleAttOption(demo, "Brand",selection + 1, currentDealerID, Make);
                    break;

                case 4:
                    Year = alterVehicleAttOption(demo, "Year",selection + 1, currentDealerID, Make);
                    break;

                case 5:
                    Engine = alterVehicleAttOption(demo, "Engine",selection + 1, currentDealerID, Make);
                    break;

                case 6:
                    Color = alterVehicleAttOption(demo, "Color",selection + 1, currentDealerID, Make);
                    break;

                case 7:
                    Transmission = alterVehicleAttOption(demo, "Transmission",selection + 1, currentDealerID, Make);
                    break;

                case 8:
                    Drive_Type = alterVehicleAttOption(demo, "Drive_Type",selection + 1, currentDealerID, Make);
                    break;

                case 9:
                    Price = alterVehicleNumericAtt(demo,selection + 1, currentDealerID, "Price");
                    break;
                case 10:
                    Miles = alterVehicleNumericAtt(demo, selection + 1, currentDealerID, "Miles");
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
    private static String alterVehicleAttOption(H2DemoMain demo, String optionToChange, int thingToChangeColumn, String currentDealerID, String currentMake){
        ArrayList<String > optionsToChooseFrom = new ArrayList<String>();
        ArrayList<String > optionsToChooseFromMakes = new ArrayList<String>();

        try {
            String query = "SELECT * FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN "
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\' "
                    + "ORDER BY vehicles.Make asc;";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                if (!optionsToChooseFrom.contains(result.getString(thingToChangeColumn))){
                        optionsToChooseFromMakes.add(" made by " + result.getString(2));
                        optionsToChooseFrom.add(result.getString(thingToChangeColumn));
                }
            }
        } catch (java.sql.SQLException e){}

        System.out.println("\n--" + optionToChange + " To Choose From--");
        if (thingToChangeColumn == 3 && !currentMake.equals("")) {
            System.out.println("Note your currently selected make is: " + currentMake);
        }
        System.out.println("----------------");
        int i;
        for(i = 0; i < optionsToChooseFrom.size(); i++){
            if (thingToChangeColumn == 3){
                System.out.println(i + ": " + optionsToChooseFrom.get(i) + optionsToChooseFromMakes.get(i));
            }
            else
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

    /*
     * Helper fn to alter numberic values
     */
    private static String alterVehicleNumericAtt(H2DemoMain demo, int thingToChangeColumn, String currentDealerID, String thingToChange){
        int value = -1;
        int greaterOrLess = -1;
        String optionToChange;

        try {
            String query = "SELECT MIN(" + thingToChange + ") FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN"
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\'";

            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);
            result.next();
            System.out.println("Min " + thingToChange +": " + result.getString(1));

            query = "SELECT MAX(" + thingToChange + ") FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN"
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\'";

            stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeQuery(query);
            result.next();
            System.out.println("Max " + thingToChange +": " + result.getString(1));

            query = "SELECT AVG(" + thingToChange + ") FROM vehicles "
                    + "INNER JOIN DealerVehicleInventory ON" +
                    " DealerVehicleInventory.VIN = vehicles.VIN"
                    + " WHERE DealerVehicleInventory.Dealer_ID =\'"
                    + currentDealerID + "\'";

            stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeQuery(query);
            result.next();
            System.out.println("Average " + thingToChange +": " + result.getString(1));

        } catch (java.sql.SQLException e){}

        System.out.print("Input a price for your vehicle: ");
        // loop to ensure a number is inputted / no crash
        while(true) {
            String optionInput = userin.next();

            try{
                value = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e){}
            if ((value >= 0))
                break;
            System.out.print("Please input a whole number: ");
        }

        System.out.println("1: Greater than");
        System.out.println("2: Less than");
        System.out.print("Select if you want to see vehicles above or below your selected price: ");
        while(true) {
            String optionInput = userin.next();

            try{
                greaterOrLess = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e){}
            if ((greaterOrLess == 1) || (greaterOrLess == 2))
                break;
            System.out.print("Please input a 1 or 2: ");
        }

        if(greaterOrLess == 1){
            optionToChange = ">" +  "\'" + value + "\'";
        }
        else
            optionToChange = "<" +  "\'" + value + "\'";

        return optionToChange;
    }

    /*
     * used to view a specific vehicle / add it to the cart
     */
    private static ArrayList<Vehicle> viewVehicleInDetail(H2DemoMain demo, Vehicle vehicleToView,ArrayList<Vehicle> cart){
        System.out.println("\n-----Current Vehicle Selected-----");
        System.out.println("Vin: " + vehicleToView.getVIN());
        System.out.println("Make: " + vehicleToView.getMake());
        System.out.println("Model: " + vehicleToView.getModel());
        System.out.println("Brand: " + vehicleToView.getBrand());
        System.out.println("Year: " + vehicleToView.getYear());
        System.out.println("Engine: " + vehicleToView.getEngine());
        System.out.println("Color: " + vehicleToView.getColor());
        System.out.println("Transmission: " + vehicleToView.getTransmission());
        System.out.println("Drive Type: " + vehicleToView.getDrive_Type());
        System.out.println("Price: $" + vehicleToView.getPrice());
        System.out.println("Miles: " + vehicleToView.getMiles());

        System.out.println("\n--Options--");
        System.out.println("1: Add car to cart");
        System.out.println("2: Go back to dealer menu");
        System.out.print("Select an option: ");

        int option = -1;
        // loop to ensure a number is inputted / no crash
        while(true) {
            String optionInput = userin.next();

            try{
                option = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e){}
            if ((option >= 1) && (option <= 3))
                break;
            System.out.print("Please input a number from the list above: ");
        }

        if (option == 1)
            cart.add(vehicleToView);
        System.out.println("Car added to your cart");

        return cart;
    }

    public static ArrayList<Vehicle> viewCart(H2DemoMain demo, ArrayList<Vehicle> cart, Customer currentCustomer, String currentDealerID){
        while (true) {

            if (cart.size() == 0) {
                System.out.println("\n-----Current Vehicles in Cart-----");
                System.out.println("\nNo vehicles in your cart");
                return cart;
            }
            int total = 0;

            System.out.println("\n-----Current Vehicles in Cart-----");

            System.out.printf("%-20s %-5s %-20s %-30s %-7s\n",
                    "Color", "Year", "Make",
                    "Model", "Price");

            for (int i = 0; i < cart.size(); i++) {
                System.out.printf("%-20s %-5s %-20s %-30s %-7s\n",
                        cart.get(i).getColor(), cart.get(i).getYear(), cart.get(i).getMake(),
                        cart.get(i).getModel(), "$" + cart.get(i).getPrice());

                total += cart.get(i).getPrice();
            }
            System.out.println("-----");
            System.out.println("Total: $" + total);
            System.out.println("-----");

            System.out.println("1: Buy all vehicles in the cart");
            System.out.println("2: Remove vehicles from the cart");
            System.out.println("3: Go back to dealer menu");
            System.out.print("Select an option: ");

            int option = -1;
            // loop to ensure a number is inputted / no crash
            while (true) {
                String optionInput = userin.next();

                try {
                    option = Integer.parseInt(optionInput);
                } catch (java.lang.NumberFormatException e) {
                }
                if ((option >= 1) && (option <= 3))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (option) {
                case (1):
                    buyCars(demo,currentCustomer,currentDealerID,cart, total);
                    return (cart = new ArrayList<Vehicle>());

                case (2):
                    System.out.println("\n-----Current Vehicles in Cart Available to Remove-----");

                    System.out.printf("    %-20s %-5s %-20s %-30s %-7s\n",
                            "Color", "Year", "Make",
                            "Model", "Price");

                    int i = 0;
                    for (i = 0; i < cart.size(); i++) {
                        System.out.printf("%2d: %-20s %-5s %-20s %-30s %-7s\n",
                                i,cart.get(i).getColor(), cart.get(i).getYear(), cart.get(i).getMake(),
                                cart.get(i).getModel(), "$" + cart.get(i).getPrice());
                    }

                    System.out.println(" "+ i + ": Go back");
                    System.out.print("Select a vehicle to remove: ");

                    int option1 = -1;
                    // loop to ensure a number is inputted / no crash
                    while (true) {
                        String optionInput = userin.next();

                        try {
                            option1 = Integer.parseInt(optionInput);
                        } catch (java.lang.NumberFormatException e) {
                        }
                        if ((option1 >= 0) && (option1 <= cart.size()))
                            break;
                        System.out.print("Please input a number from the list above: ");
                    }

                    if (option1 < cart.size()) {
                        System.out.println("\nRemoving " + cart.get(option1).getMake() + " "+
                                cart.get(option1).getModel());
                        cart.remove(option1);
                    }
                    break;

                case (3):
                    return cart;
            }
        }
    }


    private static void buyCars(H2DemoMain demo, Customer ccurrentCustomer, String currentDealer, ArrayList<Vehicle> cart, int total){
        for (int i = 0; i < cart.size(); i++){
            String charsToSelectFrom = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
            String newSaleID = "";

            // getting new Sale ID.
            while (true) {
                newSaleID = "";
                for (int x = 0; x < 7; x++){
                    int index = (int)(Math.random() * ((charsToSelectFrom.length())));
                    newSaleID = newSaleID + charsToSelectFrom.substring(index,index+1);
                }

                try {
                    String query = "SELECT * FROM customer_sale_table WHERE customer_sale_table.Sale_ID =\'" + newSaleID + "\';";
                    Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result = stmt.executeQuery(query);

                    if (!result.isBeforeFirst())
                        break;
                } catch (java.sql.SQLException e){}
            }

            CustomerSaleTable.addCustomerSale(demo.getConnection(), newSaleID,ccurrentCustomer.getSSN(),currentDealer,System.currentTimeMillis(),total);

            VehiclesSoldToCustomerTable.addVehicleSoldToCustomer(demo.getConnection(),newSaleID,cart.get(i).getVIN());

            DealerVehicleInventoryTable.removeVehicleFromDealerInventory(demo.getConnection(),cart.get(i).getVIN());
            System.out.println("\nVehicles bought! Thank you for your purchase!");
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

        userin.close();
        demo.closeConnection();
    }
}

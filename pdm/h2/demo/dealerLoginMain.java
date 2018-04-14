package pdm.h2.demo;

import pdm.h2.demo.objects.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Driver class for the Dealer's login. Handles the Dealer UI
 * after a dealer logs in. Builds a UI for user and handles passing
 * around of each method called.
 * @Author Andrew Necoechea
 * @Author Emerson Bolha
 */
public class dealerLoginMain {

    private static Scanner userIn = new Scanner(System.in);


    /**
     * Creates initial dealer object which is passed through
     * rest of the program
     */
    private static Dealer loginDealer(H2DemoMain demo) {

        int choice;
        Dealer currentDealer = null;
        System.out.println("\n-----Dealer Login-----");

        while (true) {
            System.out.print("Enter your Dealer ID:");
            String dealerID = userIn.next();


            String query = "SELECT * FROM dealer WHERE dealer.Dealer_ID=\'" + dealerID +"\';";

            try {

                // same code used in the customer login. Used in this just to check if the login exists.
                Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet result = stmt.executeQuery(query);
                result.last();
                int numOfPPL = result.getRow();
                result.beforeFirst();

                if (numOfPPL == 0){
                    System.out.println("Incorrect Dealer login, please try again.");
                    continue;
                }

                if (numOfPPL == 1) {
                    result.next();
                    String[] data = new String[result.getMetaData().getColumnCount()];

                    int i = 0;
                    while (i < result.getMetaData().getColumnCount()){
                        data[i] = result.getString(i+1);
                        i++;
                    }
                     currentDealer = new Dealer(data);
                    break;
                }


            } catch (SQLException e) {e.printStackTrace();}

        }
        return currentDealer;
    }

    /**
     * Dealer interaction loop that handles initial selection of what
     * option the user wants. Takes a choice in from the user and
     * uses a switch statement to select the option
     */
    private static void dealerInteractionLoop(H2DemoMain demo, Dealer currentDealer) {
        int option;

        cls.clear();
        System.out.println("\n-----" + currentDealer.getName() + " Home-----");

        option = -1;

        while (option != 5) {

            System.out.println("\n--Main Menu--");
            System.out.println("1: View Inventory");
            System.out.println("2: View Customers");
            System.out.println("3: View Manufacturers");
            System.out.println("4: View Statistics");
            System.out.println("5: Logout");
            System.out.print("Select an option: ");

            while (true) {
                String input = userIn.next();

                try{
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e){}
                if ((option > 0) && (option <= 5))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (option) {
                case 1:
                    cls.clear();
                    viewInventory(demo, currentDealer);
                    break;

                case 2:
                    cls.clear();
                    viewCustomers(demo, currentDealer);
                    break;

                case 3:
                    cls.clear();
                    viewManufacturers(demo, currentDealer);
                    break;

                case 4:
                    cls.clear();
                    statistics(demo, currentDealer);
                    break;
                case 5:
                    cls.clear();
                    System.out.println("\nGoodbye!");
                    break;
            }
        }
    }

    /**
     * This method uses an SQL query to gather the vehicles that
     * the current dealer has and displays them in a menu. The user
     * can then select a vehicle to view more information about it
     */
    private static void viewInventory(H2DemoMain demo, Dealer currentDealer) {
        ArrayList<Vehicle> vehicleInventory = new ArrayList<Vehicle>();
        boolean loopThrough = true;

        try {
            String queryVehicles = "SELECT * from DealerVehicleInventory"
                    + " INNER JOIN vehicles ON vehicles.VIN = DealerVehicleInventory.VIN WHERE Dealer_ID = \'" + currentDealer.getDealer_ID() +"\'" ;
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queryVehicles);
            while (result.next()) {
                Vehicle newVehicle = new Vehicle(result.getString("VIN"),result.getString("Make"),
                        result.getString("Model"),result.getString("Brand"),result.getInt("Year"),
                        result.getString("Engine"),result.getString("Color"), result.getString("Transmission"),
                        result.getString("Drive_Type"), result.getInt("Price"), result.getInt("Miles"));
                vehicleInventory.add(newVehicle);
            }




            while (loopThrough) {
                System.out.println("\n-----VEHICLE INVENTORY-----");
                System.out.printf("%2s: %20s %10s %20s %30s %15s %15s %15s %15s %15s\n",
                        "--","Color","Year","Make","Model","Engine","Transmission","Drive Type","Price","Miles");
                System.out.println("----------------------------------------------------------------------------" +
                        "-------------------------------------------------" +
                        "----------------------------------------------");
                for (int i = 0; i < vehicleInventory.size();i++){
                    //System.out.println(i + 1 + ": " + vehicleInventory.get(i).getYear() + " " + vehicleInventory.get(i).getMake() + " " + vehicleInventory.get(i).getModel());
                    System.out.printf("%2d: %20s %10s %20s %30s %15s %15s %15s %15s %15s Mi.",i + 1,
                            vehicleInventory.get(i).getColor(),
                            vehicleInventory.get(i).getYear(),
                            vehicleInventory.get(i).getMake(),
                            vehicleInventory.get(i).getModel(),
                            vehicleInventory.get(i).getEngine(),
                            vehicleInventory.get(i).getTransmission(),
                            vehicleInventory.get(i).getDrive_Type(),
                            vehicleInventory.get(i).getPrice(),
                            vehicleInventory.get(i).getMiles());
                    System.out.println("\n----------------------------------------------------------------------------" +
                            "-------------------------------------------------" +
                            "----------------------------------------------");

                }
                System.out.println(vehicleInventory.size() + 1 + ": Return to prev menu");
                System.out.println("------------------------");

                int choice = -1;


                System.out.print("Please select an option from the list above: ");
                while (true) {

                    String input = userIn.next();


                    try {
                        choice = Integer.parseInt(input);
                    } catch (java.lang.NumberFormatException e){}

                    if (choice > 0 && choice <= vehicleInventory.size()) {
                        cls.clear();
                        showVehicleInformation(vehicleInventory.get(choice - 1));
                        break;
                    }
                    if (choice == vehicleInventory.size() + 1) {
                        cls.clear();
                        return;
                    }
                    else {
                        System.out.print("Please input a number from the list above: ");
                    }

                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    /**
     * This method displays the cusomters that each dealership has.
     * The dealer can select a customer and then view more information
     * if they desire.
     */
    private static void viewCustomers(H2DemoMain demo, Dealer currentDealer) {
        ArrayList<Customer> customerList = new ArrayList<Customer>();
        boolean loopThrough = true;

        try {

            String queuryCustomers = "SELECT * from customer"
                    + " INNER JOIN customer_sale_table ON customer_sale_table.SSN = customer.SSN WHERE customer_sale_table.Dealer_ID = \'"
                    + currentDealer.getDealer_ID() +"\'";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queuryCustomers);

            while (result.next()) {
                Customer newCustomer = new Customer(result.getString("SSN"), result.getString("Name"),result.getString("Gender"),
                        result.getInt("Income"), result.getInt("House_Num"),result.getString("Street"),
                        result.getString("City"),result.getString("State"),result.getString("ZIP"),result.getString("Email"),
                        result.getString("Password"));
                customerList.add(newCustomer);
            }

            while (loopThrough) {
                System.out.println("\n-----CUSTOMERS-----");
                System.out.printf("%2s: %50s %20s %40s\n",
                        "--","Name","SSN" ,"Email" );
                System.out.println("----------------------------------------------------------------------------" +
                        "-------------------------------------------------" +
                        "--------------------------------------");
                for (int i = 0; i < customerList.size();i++) {

                    //System.out.println(i + 1 + ": " + customerList.get(i).getName() + " " + customerList.get(i).getSSN());
                    System.out.printf("%2d: %50s %20s %40s", i + 1,
                                        customerList.get(i).getName(),
                                        customerList.get(i).getSSN(),
                                        customerList.get(i).getEmail());

                    System.out.println("\n----------------------------------------------------------------------------" +
                            "-------------------------------------------------" +
                            "--------------------------------------");
                }

                System.out.println(customerList.size() + 1 + ": Return to prev menu");
                System.out.println("------------------------");

                int choice = -1;

                System.out.print("Please select an option from the list above: ");
                while (true) {

                    //System.out.print("Please select an option from the list above: ");
                    String input = userIn.next();


                    try {
                        choice = Integer.parseInt(input);
                    } catch (java.lang.NumberFormatException e){}

                    if (choice > 0 && choice <= customerList.size()) {
                        cls.clear();
                        showCustomerInformation(customerList.get(choice - 1), demo);
                        break;
                    }
                    if (choice == customerList.size() + 1) {
                        cls.clear();
                        return;
                    }
                    else {
                        System.out.print("Please input a number from the list above: ");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows the dealer to view the selection of manufacturers.
     * Once a manufacturer is selected the dealer can interact to buy vehicles
     * similar to how the customer can buy vehicles from the dealers.
     */
    private static void viewManufacturers(H2DemoMain demo, Dealer currentDealer) {
        ArrayList<Manufacturer> manufacturers = new ArrayList<Manufacturer>();

        try {
            String queryManufacturers = "SELECT * from Manufacturer";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queryManufacturers);

            while (result.next()) {
                Manufacturer newManufacturer = new Manufacturer(result.getString("M_ID"),result.getString("Name"));
                manufacturers.add(newManufacturer);
            }

            while (true) {
                for (int i = 0; i < manufacturers.size(); i++) {
                    System.out.println(i + 1 + ": " + manufacturers.get(i).getName());
                }

                System.out.println(manufacturers.size() + 1 + ": Prior Menu");

                int choice = -1;

                System.out.println("\n-----Manufacturer Selection-----");

                while (true) {

                    System.out.print("Please select an option from the list above: ");
                    String input = userIn.next();


                    try {
                        choice = Integer.parseInt(input);
                    } catch (java.lang.NumberFormatException e){}

                    if (choice > 0 && choice <= manufacturers.size()) {
                        break;
                    }
                    if (choice == manufacturers.size() + 1) {
                        cls.clear();
                        return;
                    }
                    else {
                        System.out.print("Please input a number from the list above: ");
                    }
                }

                cls.clear();
                System.out.println(manufacturers.get(choice - 1).getName() + " was selected");

                boolean stayInLoop = true;
                ArrayList<String> whereClauseArrayList = new ArrayList<String>();

                for (int i = 0; i < 10; i++) {
                    whereClauseArrayList.add("");
                }
                ArrayList<Vehicle> cart = new ArrayList<Vehicle>();


                while (stayInLoop) {
                    int selection = -1;
                    System.out.println("\n--" + manufacturers.get(choice - 1).getName() + "--");
                    System.out.println("1: View vehicles");
                    System.out.println("2: Alter search options");
                    System.out.println("3: View cart (" + cart.size() + " cars in cart)");
                    System.out.println("4: Previous menu");
                    System.out.print("Select an option: ");

                    while (true) {
                        String optionInput = userIn.next();
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
                            cls.clear();
                            System.out.println("\n-----Vehicles to Buy-----");
                            cart = viewVehiclesToBuy(demo, whereClauseArrayList, manufacturers.get(choice - 1).getM_ID(), cart);
                            break;

                        case 2:
                            cls.clear();
                            whereClauseArrayList =
                                   alterWhereClause(demo, whereClauseArrayList, manufacturers.get(choice - 1).getM_ID());
                            break;

                        case 3:
                            cls.clear();
                            cart = viewCart(demo,cart,currentDealer,manufacturers.get(choice - 1).getM_ID());
                            break;

                        case 4:
                            cls.clear();
                            stayInLoop = false;
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This allows the user to edit the selections for buying vehicles
     * from the manufacturer. This method returns an array list so that
     * the SQL statements can be modified for custom searches.
     */
    private static ArrayList<String> alterWhereClause(H2DemoMain demo, ArrayList<String> whereClauseArrayList, String currentManufacturerID){
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

        cls.clear();
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
                String optionInput = userIn.next();
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
                    cls.clear();
                    Make = alterVehicleAttOption(demo, "Make",selection + 1, currentManufacturerID, Make);
                    break;

                case 2:
                    cls.clear();
                    Model = alterVehicleAttOption(demo, "Model",selection + 1, currentManufacturerID, Make);
                    break;

                case 3:
                    cls.clear();
                    Brand = alterVehicleAttOption(demo, "Brand",selection + 1, currentManufacturerID, Make);
                    break;

                case 4:
                    cls.clear();
                    Year = alterVehicleAttOption(demo, "Year",selection + 1, currentManufacturerID, Make);
                    break;

                case 5:
                    cls.clear();
                    Engine = alterVehicleAttOption(demo, "Engine",selection + 1, currentManufacturerID, Make);
                    break;

                case 6:
                    cls.clear();
                    Color = alterVehicleAttOption(demo, "Color",selection + 1, currentManufacturerID, Make);
                    break;

                case 7:
                    cls.clear();
                    Transmission = alterVehicleAttOption(demo, "Transmission",selection + 1, currentManufacturerID, Make);
                    break;

                case 8:
                    cls.clear();
                    Drive_Type = alterVehicleAttOption(demo, "Drive_Type",selection + 1, currentManufacturerID, Make);
                    break;

                case 9:
                    cls.clear();
                    Price = alterVehicleNumericAtt(demo,selection + 1, currentManufacturerID, "Price");
                    break;

                case 10:
                    cls.clear();
                    Miles = alterVehicleNumericAtt(demo, selection + 1, currentManufacturerID, "Miles");
                    break;

                case 11:
                    cls.clear();
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


    /**
     * Allows user to alter the SQL query for specific string
     * custom searches. Eg. make, model, brand, etc.
     */
    private static String alterVehicleAttOption(H2DemoMain demo, String optionToChange, int thingToChangeColumn, String currentManufacturerID, String currentMake){
        ArrayList<String > optionsToChooseFrom = new ArrayList<String>();
        ArrayList<String > optionsToChooseFromMakes = new ArrayList<String>();

        try {
            String query = "SELECT * FROM vehicles "
                    + "INNER JOIN ManufacturerVehicles ON" +
                    " ManufacturerVehicles.VIN = vehicles.VIN "
                    + " WHERE ManufacturerVehicles.M_ID =\'"
                    + currentManufacturerID + "\' "
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
            String optionInput = userIn.next();
            try {
                userSelection = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e) {
            }
            if ((userSelection >= 0) && (userSelection < optionsToChooseFrom.size() + 1))
                break;
            System.out.print("Please input a number from the list above: ");
        }

        cls.clear();

        if(userSelection == optionsToChooseFrom.size()) {
            System.out.println("No preference selected for " + optionToChange + ".");
            return "";
        }
        else {
            System.out.println("You selected " + optionsToChooseFrom.get(userSelection) + " for your " + optionToChange + ".");
            return optionsToChooseFrom.get(userSelection);
        }
    }

    /**
     * This method allows the user to edit specific
     * numerical values when doing a custom search.
     */
    private static String alterVehicleNumericAtt(H2DemoMain demo, int thingToChangeColumn, String currentManufacturerID, String thingToChange){
        int value = -1;
        int greaterOrLess = -1;
        String optionToChange;

        try {
            String query = "SELECT MIN(" + thingToChange + ") FROM vehicles "
                    + "INNER JOIN ManufacturerVehicles ON" +
                    " ManufacturerVehicles.VIN = vehicles.VIN"
                    + " WHERE ManufacturerVehicles.M_ID =\'"
                    + currentManufacturerID + "\'";

            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);
            result.next();
            System.out.println("Min " + thingToChange +": " + result.getString(1));

            query = "SELECT MAX(" + thingToChange + ") FROM vehicles "
                    + "INNER JOIN ManufacturerVehicles ON" +
                    " ManufacturerVehicles.VIN = vehicles.VIN"
                    + " WHERE ManufacturerVehicles.M_ID =\'"
                    + currentManufacturerID + "\'";

            stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeQuery(query);
            result.next();
            System.out.println("Max " + thingToChange +": " + result.getString(1));

            query = "SELECT AVG(" + thingToChange + ") FROM vehicles "
                    + "INNER JOIN ManufacturerVehicles ON" +
                    " ManufacturerVehicles.VIN = vehicles.VIN"
                    + " WHERE ManufacturerVehicles.M_ID =\'"
                    + currentManufacturerID + "\'";

            stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeQuery(query);
            result.next();
            System.out.println("Average " + thingToChange +": " + result.getString(1));

        } catch (java.sql.SQLException e){}

        System.out.print("Input a price for your vehicle: ");
        // loop to ensure a number is inputted / no crash
        while(true) {
            String optionInput = userIn.next();

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
            String optionInput = userIn.next();

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

    /**
     * This method creates the vehicles arraylist that the user
     * can purchase after a query has been performed on them.
     */
    private static ArrayList<Vehicle> viewVehiclesToBuy(H2DemoMain demo, ArrayList<String> whereClauseArrayList, String currentMID, ArrayList<Vehicle> cart){
        ResultSet result;
        ArrayList<Vehicle> vehiclesToSelect = new ArrayList<Vehicle>();
        int i = 1;

        try {
            String query = "SELECT * FROM vehicles "
                    + "INNER JOIN ManufacturerVehicles ON" +
                    " ManufacturerVehicles.VIN = vehicles.VIN"
                    + " WHERE ManufacturerVehicles.M_ID =\'"
                    + currentMID + "\'";

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
                    String optionInput = userIn.next();

                    try{
                        option = Integer.parseInt(optionInput);
                    } catch (java.lang.NumberFormatException e){}
                    if ((option >= 1) && (option <= i))
                        break;
                    System.out.print("Please input a number from the list above: ");
                }
                if (option != i){
                    cls.clear();
                    viewVehicleInDetail(demo, vehiclesToSelect.get(option-1), cart);
                }
            }
        } catch (java.sql.SQLException e){}
        return cart;
    }

    /**
     * This displays the vehicles that the user can purchase.
     * It allows the vehicles to be added to the cart or the
     * user can go back to the prior menu.
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
        System.out.println("2: Go back to manufacturer menu");
        System.out.print("Select an option: ");

        int option = -1;
        // loop to ensure a number is inputted / no crash
        while(true) {
            String optionInput = userIn.next();

            try{
                option = Integer.parseInt(optionInput);
            } catch (java.lang.NumberFormatException e){}
            if ((option >= 1) && (option <= 3))
                break;
            System.out.print("Please input a number from the list above: ");
        }

        cls.clear();
        if (option == 1) {
            cart.add(vehicleToView);
            System.out.println("Car added to your cart");
        }
        return cart;
    }

    /**
     * This method displays the user's cart. In the cart
     * they can view the vehicles, remove them from the cart
     * or buy the vehicles.
     */
    public static ArrayList<Vehicle> viewCart(H2DemoMain demo, ArrayList<Vehicle> cart, Dealer currentDealer, String currentMID){
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
                String optionInput = userIn.next();

                try {
                    option = Integer.parseInt(optionInput);
                } catch (java.lang.NumberFormatException e) {
                }
                if ((option >= 1) && (option <= 3))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            cls.clear();
            switch (option) {
                case (1):
                    buyCars(demo,currentDealer,currentMID,cart, total);

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
                        String optionInput = userIn.next();

                        try {
                            option1 = Integer.parseInt(optionInput);
                        } catch (java.lang.NumberFormatException e) {
                        }
                        if ((option1 >= 0) && (option1 <= cart.size()))
                            break;
                        System.out.print("Please input a number from the list above: ");
                    }

                    cls.clear();
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

    /**
     * This method takes the cart and then buys the vehicles.
     * A new sale ID is created and the vehicle data is removed
     * from the manufacturer's table and placed into the dealer's.
     */
    private static void buyCars(H2DemoMain demo, Dealer currentDealer, String currentManufacturer, ArrayList<Vehicle> cart, int total){
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
                    String query = "SELECT * FROM VehiclesSoldToDealers WHERE VehiclesSoldToDealers.Dealer_Sale_ID =\'" + newSaleID + "\';";    // TODO EDIT THIS
                    Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result = stmt.executeQuery(query);

                    if (!result.isBeforeFirst())
                        break;
                } catch (java.sql.SQLException e){}
            }

            DealerSaleTable.addDealerSale(demo.getConnection(),newSaleID,currentDealer.getDealer_ID(),currentManufacturer,System.currentTimeMillis(),total);

            VehiclesSoldToDealersTable.addVehiclesSoldToDealers(demo.getConnection(),newSaleID,cart.get(i).getVIN());

            ManufacturerVehiclesTable.removeVehicleFromManufacturerInventory(demo.getConnection(),cart.get(i).getVIN());

            DealerVehicleInventoryTable.addDealerVehicleInventory(demo.getConnection(),currentDealer.getDealer_ID(),cart.get(i).getVIN());

            System.out.println("\nVehicles bought! Thank you for your purchase!");
        }
    }

    /**
     * This method displays all relevant information about
     * the customer that was selected.
     */
    private static void showCustomerInformation(Customer currentCustomer, H2DemoMain demo) {

        ArrayList<String> phoneNumbers = new ArrayList<String>();

        try {

            String queuryCustomerPhones = "SELECT Phone_Num from customer_phone" +
                    " WHERE customer_phone.SSN = \'" + currentCustomer.getSSN() + "\'";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queuryCustomerPhones);
            while (result.next()){
                phoneNumbers.add(result.getString("Phone_Num"));
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n-----Current Customer Selected-----");
        System.out.println("SSN: " + currentCustomer.getSSN());
        System.out.println("Name: " + currentCustomer.getName());
        System.out.println("Gender: " + currentCustomer.getGender());
        System.out.println("Income: $" + currentCustomer.getIncome());
        System.out.println("Address: " + currentCustomer.gethouse_num() + " "
                + currentCustomer.getStreet() + " " + currentCustomer.getCity() + " " + currentCustomer.getState()+ " " + currentCustomer.getZip());
        System.out.println("Email: " + currentCustomer.getEmail());
        System.out.print("Phone Number(s): ");
        for (int i = 0; i < phoneNumbers.size();i++) {
            System.out.print(" " + phoneNumbers.get(i));
        }

        System.out.println("\n-----Options-----");
        System.out.println("1: Go back to customer menu");
        int choice = -1;
        while (choice != 1) {
            try {
                System.out.print("Select an option: ");
                choice = Integer.parseInt(userIn.next());
            } catch (java.lang.NumberFormatException e){}
        }
        cls.clear();
    }

    /**
     * This method displays the vehicle information
     * when it is selected from the dealer's inventory.
     * Option to purchase a vehicle is obviously lacking
     * in this method.
     */
    private static void showVehicleInformation(Vehicle currentVehicle){

        System.out.println("\n-----Current Vehicle Selected-----");
        System.out.println("VIN: " + currentVehicle.getVIN());
        System.out.println("Make: " + currentVehicle.getMake());
        System.out.println("Model: " + currentVehicle.getModel());
        System.out.println("Brand: " + currentVehicle.getBrand());
        System.out.println("Year: " + currentVehicle.getYear());
        System.out.println("Engine: " + currentVehicle.getEngine());
        System.out.println("Color: " + currentVehicle.getColor());
        System.out.println("Transmission: " + currentVehicle.getTransmission());
        System.out.println("Drive Type: " + currentVehicle.getDrive_Type());
        System.out.println("Price: $" + currentVehicle.getPrice());
        System.out.println("Miles: " + currentVehicle.getMiles());

        System.out.println("\n--Options--");
        System.out.println("1: Go back to inventory menu");
        int choice = -1;
        while (choice != 1) {
            try {
                System.out.print("Select an option: ");
                choice = Integer.parseInt(userIn.next());
            } catch (java.lang.NumberFormatException e){}
        }
        cls.clear();
    }

    /**
     * This is the main method for displaying statistics
     * for the dealerships. Allows user to select an option
     * and uses a switch statement to call the other needed
     * methods.
     */
    private static void statistics(H2DemoMain demo, Dealer currentDealer) {

        while (true) {

            System.out.println("\n-----Statistics-----");
            System.out.println("1: Sales by Make and Gender");
            System.out.println("2: Overall Sales by Each Dealer");
            System.out.println("3: Vehicles Sold by Each Dealer");
            System.out.println("4: Brand, Make, Model Stats");
            System.out.println("5: Brand, Make, Model Count");
            System.out.println("6: Prior Menu");

            System.out.println("-------------------");
            System.out.print("Make a selection: ");

            int option = - 1;

            while (true) {
                String input = userIn.next();

                try{
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e){}
                if ((option > 0) && (option <= 6))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            cls.clear();
            switch (option) {
                case 1:
                    makeAndGender(demo, currentDealer);
                    break;
                case 2:
                    overAllSalesForAllDealers(demo);
                    break;
                case 3:
                    carsSoldStats(demo);
                    break;
                case 4:
                    brandMakeModelStats(demo);
                    break;
                case 5:
                    brandMakeModelCount(demo);
                    break;
                case 6:
                    return;
            }
        }
    }

    /**
     * Displays information for the current dealer
     * in regards to the sales of vehicles based upon
     * the make of the vehicles, the gender of the buyer,
     * and the total amount of money made.
     */
    public static void makeAndGender(H2DemoMain demo, Dealer currentDealer) {

        try {

            String queryMakeAndGender = "SELECT vehicles.Make, vehicles.VIN, customer.Gender, SUM(customer_sale_table.Total) FROM vehicles"
                    + " INNER JOIN vehicles_sold_to_customer ON vehicles_sold_to_customer.VIN = vehicles.VIN"
                    + " INNER JOIN customer_sale_table ON customer_sale_table.Sale_ID = vehicles_sold_to_customer.Sale_ID"
                    + " INNER JOIN customer ON customer.SSN = customer_sale_table.SSN WHERE customer_sale_table.Dealer_ID =\'" + currentDealer.getDealer_ID()
                    + "\' GROUP BY vehicles.Make, vehicles.VIN, customer.Gender"
                    + " ORDER BY Make;";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queryMakeAndGender);
            System.out.printf("\n-----Sales by Make and Gender-----\n");
            System.out.printf("%-20s %-10s %-20s\n", "Make", "Gender", "Money Made");
            while(result.next()) {
                System.out.printf("%-20s %-10s $%-20s\n", result.getString(1), result.getString(3), result.getString(4));
            }
            System.out.print("\n");


        } catch(SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * Displays information about the number
     * of cars that have been sold by the dealerships.
     */
    private static void carsSoldStats(H2DemoMain demo) {

        try {

            String queryCarsSold = "SELECT DEALER.NAME, count(CUSTOMER_SALE_TABLE.TOTAL) as \"Cars Sold\"\n" +
                    "FROM VEHICLES\n" +
                    "\n" +
                    "INNER JOIN VEHICLES_SOLD_TO_CUSTOMER ON VEHICLES_SOLD_TO_CUSTOMER.VIN = VEHICLES.VIN\n" +
                    "INNER JOIN CUSTOMER_SALE_TABLE ON CUSTOMER_SALE_TABLE.SALE_ID = VEHICLES_SOLD_TO_CUSTOMER.SALE_ID\n" +
                    "INNER JOIN CUSTOMER ON CUSTOMER.SSN = CUSTOMER_SALE_TABLE.SSN\n" +
                    "\n" +
                    "INNER JOIN DEALER ON DEALER.DEALER_ID = CUSTOMER_SALE_TABLE.DEALER_ID\n" +
                    "\n" +
                    "INNER JOIN VEHICLESSOLDTODEALERS ON VEHICLESSOLDTODEALERS.VIN = VEHICLES_SOLD_TO_CUSTOMER.VIN\n" +
                    "INNER JOIN DEALER_SALE ON DEALER_SALE.DEALER_SALE_ID = VEHICLESSOLDTODEALERS.DEALER_SALE_ID\n" +
                    "GROUP BY DEALER.NAME\n";
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(queryCarsSold);
            System.out.printf("\n-----Vehicles Sold by Each Dealer-----\n");
            System.out.printf("%-40s %-20s\n","Dealer", "Vehicles Sold");

            while(result.next()){
                System.out.printf("%-40s %-20d\n",result.getString(1), result.getInt(2));
            }
            System.out.print("\n");

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the dealers name as well as the
     * profit they have made.
     */
    private static void overAllSalesForAllDealers(H2DemoMain demo){
        String query = "SELECT DEALER.NAME, (SUM(CUSTOMER_SALE_TABLE.TOTAL) - SUM(DEALER_SALE.TOTAL)) AS \"Total income\"\n" +
                "FROM VEHICLES\n" +
                "\n" +
                "INNER JOIN VEHICLES_SOLD_TO_CUSTOMER ON VEHICLES_SOLD_TO_CUSTOMER.VIN = VEHICLES.VIN\n" +
                "INNER JOIN CUSTOMER_SALE_TABLE ON CUSTOMER_SALE_TABLE.SALE_ID = VEHICLES_SOLD_TO_CUSTOMER.SALE_ID\n" +
                "INNER JOIN CUSTOMER ON CUSTOMER.SSN = CUSTOMER_SALE_TABLE.SSN\n" +
                "\n" +
                "INNER JOIN DEALER ON DEALER.DEALER_ID = CUSTOMER_SALE_TABLE.DEALER_ID\n" +
                "\n" +
                "INNER JOIN VEHICLESSOLDTODEALERS ON VEHICLESSOLDTODEALERS.VIN = VEHICLES_SOLD_TO_CUSTOMER.VIN\n" +
                "INNER JOIN DEALER_SALE ON DEALER_SALE.DEALER_SALE_ID = VEHICLESSOLDTODEALERS.DEALER_SALE_ID\n" +
                "GROUP BY DEALER.NAME\n" +
                "order by \"Total income\" desc\n;";

        try{
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);

            System.out.printf("\n-----Overall Sales by Each Dealer-----\n");
            System.out.printf("%-40s %-20s\n","Dealer", "Money Made");
            while (result.next()){
                System.out.printf("%-40s $%-20d\n",result.getString(1), result.getInt(2));
            }
            System.out.print("\n");

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Displays data based on the brand, make,
     * and model total sales.
     */
    private static void brandMakeModelStats(H2DemoMain demo){
        String query = "SELECT VEHICLES.brand, vehicles.make, vehicles.model, SUM(TOTAL) AS \"Total Sales\"\n" +
                "FROM VEHICLES\n" +
                "INNER JOIN VEHICLES_SOLD_TO_CUSTOMER ON VEHICLES_SOLD_TO_CUSTOMER.VIN = VEHICLES.VIN\n" +
                "INNER JOIN CUSTOMER_SALE_TABLE ON CUSTOMER_SALE_TABLE.SALE_ID = VEHICLES_SOLD_TO_CUSTOMER.SALE_ID\n" +
                "INNER JOIN CUSTOMER ON CUSTOMER.SSN = CUSTOMER_SALE_TABLE.SSN\n" +
                "GROUP BY VEHICLES.brand, vehicles.make, vehicles.model\n" +
                "ORDER BY \"Total Sales\" desc\n;";

        try {
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);

            System.out.printf("\n-----Over all Sales for Each Model-----\n");
            System.out.printf("%-20s %-20s %-30s %-15s\n","Brand","Make","Model", "Total Sales");
            System.out.printf("----------------------------------------------------------------------------------------\n");
            while (result.next()){
                System.out.printf("%-20s %-20s %-30s $%-15d\n", result.getString(1), result.getString(2), result.getString(3), result.getInt(4));
                System.out.printf("----------------------------------------------------------------------------------------\n");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Displays the number of vehicles sold by
     * brand, make, and model.
     */
    private static void brandMakeModelCount(H2DemoMain demo) {
        String query = "SELECT VEHICLES.brand, vehicles.make, vehicles.model, count(TOTAL) AS \"Cars Sold\"\n" +
                "FROM VEHICLES\n" +
                "INNER JOIN VEHICLES_SOLD_TO_CUSTOMER ON VEHICLES_SOLD_TO_CUSTOMER.VIN = VEHICLES.VIN\n" +
                "INNER JOIN CUSTOMER_SALE_TABLE ON CUSTOMER_SALE_TABLE.SALE_ID = VEHICLES_SOLD_TO_CUSTOMER.SALE_ID\n" +
                "INNER JOIN CUSTOMER ON CUSTOMER.SSN = CUSTOMER_SALE_TABLE.SSN\n" +
                "GROUP BY VEHICLES.brand, vehicles.make, vehicles.model\n" +
                "ORDER BY \"Cars Sold\" desc\n";


        try {
            Statement stmt = demo.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = stmt.executeQuery(query);
            System.out.printf("\n-----Over all Count for Each Model-----\n");
            System.out.printf("%-20s %-20s %-30s %-15s\n", "Brand", "Make", "Model", "Vehicles Sold");
            System.out.printf("----------------------------------------------------------------------------------------\n");

            while(result.next()) {
                System.out.printf("%-20s %-20s %-30s %-25d\n", result.getString(1), result.getString(2),result.getString(3),result.getInt(4) );
                System.out.printf("----------------------------------------------------------------------------------------\n");
            }

            System.out.print("\n");

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to run the initial demo and user.
     */
    public static void main(String[] args) {
        H2DemoMain demo = new H2DemoMain();
        String location = System.getProperty("user.dir") + "/test";
        String user = "scj";
        String password = "password";
        //Create the database connections
        demo.createConnection(location, user, password);

        Dealer currentDealer = loginDealer(demo);
        dealerInteractionLoop(demo,currentDealer);
    }
}

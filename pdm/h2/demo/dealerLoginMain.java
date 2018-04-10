package pdm.h2.demo;

import pdm.h2.demo.objects.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Driver class for the Dealer's login.
 * TESTING GITHUB account
 *
 */


public class dealerLoginMain {

    private static Scanner userIn = new Scanner(System.in);

    private static Dealer loginDealer(H2DemoMain demo) {

        int choice;
        Dealer currentDealer = null;

        while (true) {
            System.out.println("Enter your Dealer ID:");
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

    private static void dealerInteractionLoop(H2DemoMain demo, Dealer currentDealer) {
        int option;
        System.out.println("\n" + currentDealer.getName() + " Login");

        option = -1;

        while (option != 4) {

            System.out.println("\n--Main Menu--");
            System.out.println("1: View Inventory");
            System.out.println("2: View Customers");
            System.out.println("3: View Manufacturers");
            System.out.println("4: Logout");
            System.out.print("Select an option: ");

            while (true) {
                String input = userIn.next();

                try{
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e){}
                if ((option > 0) && (option <= 4))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (option) {
                case 1:
                    //System.out.println("\n--VEHICLE INVENTORY--");
                    viewInventory(demo, currentDealer);
                    break;

                case 2:
                    System.out.println("\n--CUSTOMERS--");
                    viewCustomers(demo, currentDealer);
                    break;

                case 3:
                    System.out.println("\n--Manufacturers--");
                    viewManufacturers(demo, currentDealer);
                    break;

                case 4:
                    System.out.println("\nGoodbye!");
                    break;



            }


        }


    }

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
                for (int i = 0; i < vehicleInventory.size();i++){
                    System.out.println(i + 1 + ": " + vehicleInventory.get(i).getYear() + " " + vehicleInventory.get(i).getMake() + " " + vehicleInventory.get(i).getModel());
                }
                System.out.println(vehicleInventory.size() + 1 + ": Prior Menu");

                int choice = -1;


                System.out.println("\n-----Car Selection-----");
                while (true) {

                    System.out.print("Please select an option: ");
                    String input = userIn.next();


                    try {
                        choice = Integer.parseInt(input);
                    } catch (java.lang.NumberFormatException e){}

                    if (choice > 0 && choice <= vehicleInventory.size()) {
                        showVehicleInformation(vehicleInventory.get(choice - 1));
                        break;
                    }
                    if (choice == vehicleInventory.size() + 1) {
                        return;
                    }
                    else {
                        System.out.println("Not a valid option.");
                    }

                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    private static void viewCustomers(H2DemoMain demo, Dealer currentDealer) {
        //TODO
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

                System.out.println("\n-----Customers-----");
                for (int i = 0; i < customerList.size();i++) {

                    System.out.println(i + 1 + ": " + customerList.get(i).getName() + " " + customerList.get(i).getSSN());
                }

                System.out.println(customerList.size() + 1 + ": Prior Menu");

                int choice = -1;

                System.out.println("\n-----Customer Selection-----");

                while (true) {

                    System.out.print("Please select an option: ");
                    String input = userIn.next();


                    try {
                        choice = Integer.parseInt(input);
                    } catch (java.lang.NumberFormatException e){}

                    if (choice > 0 && choice <= customerList.size()) {
                        showCustomerInformation(customerList.get(choice - 1), demo);
                        break;
                    }
                    if (choice == customerList.size() + 1) {
                        return;
                    }
                    else {
                        System.out.println("Not a valid option.");
                    }

                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

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
                System.out.println("\n-----Manufacturers-----");


                for (int i = 0; i < manufacturers.size(); i++) {
                    System.out.println(i + 1 + ": " + manufacturers.get(i).getName());
                }

                System.out.println(manufacturers.size() + 1 + ": Prior Menu");

                int choice = -1;

                System.out.println("\n-----Manufacturer Selection-----");

                while (true) {

                    System.out.print("Please select an option: ");
                    String input = userIn.next();


                    try {
                        choice = Integer.parseInt(input);
                    } catch (java.lang.NumberFormatException e){}

                    if (choice > 0 && choice <= manufacturers.size()) {
                        //showCustomerInformation(manufacturers.get(choice - 1), demo);
                        break;
                    }
                    if (choice == manufacturers.size() + 1) {
                        return;
                    }
                    else {
                        System.out.print("Please input a number from the list above: ");
                    }
                }

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
                    System.out.println("4: Main menu");
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
                            System.out.println("\n-----Vehicles to Buy-----");
                            cart = viewVehiclesToBuy(demo, whereClauseArrayList, manufacturers.get(choice - 1).getM_ID(), cart);
                            break;

                        case 2:
                            //whereClauseArrayList =
                            //        alterWhereClause(demo, whereClauseArrayList, manufacturers.get(choice - 1).getM_ID());
                            break;

                        case 3:
                            cart = viewCart(demo,cart,currentDealer,manufacturers.get(choice - 1).getM_ID());
                            break;

                        case 4:
                            stayInLoop = false;
                            break;
                    }

                }



            }




        } catch (SQLException e) {
            e.printStackTrace();
        }



    }



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
                    String optionInput = userIn.next();

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
            String optionInput = userIn.next();

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


        System.out.println("\n-----" + currentCustomer.getName() +"-----");
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
        System.out.println("1: Prior menu");
        int choice = -1;
        while (choice != 1) {
            try {
                System.out.print("Select an option: ");
                choice = Integer.parseInt(userIn.next());
            } catch (java.lang.NumberFormatException e){}
        }
    }

    private static void showVehicleInformation(Vehicle currentVehicle){

        System.out.println("\n-----" + currentVehicle.getYear() +" " + currentVehicle.getMake() +" " + currentVehicle.getModel() + "-----");
        System.out.println("VIN: " + currentVehicle.getVIN());
        System.out.println("Make: " + currentVehicle.getMake());
        System.out.println("Model: " + currentVehicle.getModel());
        System.out.println("Brand: " + currentVehicle.getBrand());
        /**
         * int Year;
         String Engine;
         String Color;
         String Transmission;
         String Drive_Type;
         int Price;
         int Miles;
         */
        System.out.println("Year: " + currentVehicle.getYear());
        System.out.println("Engine: " + currentVehicle.getEngine());
        System.out.println("Color: " + currentVehicle.getColor());
        System.out.println("Transmission: " + currentVehicle.getTransmission());
        System.out.println("Drive Type: " + currentVehicle.getDrive_Type());
        System.out.println("Price: $ " + currentVehicle.getPrice());
        System.out.println("Miles: " + currentVehicle.getMiles());

        System.out.println("\n-----Options-----");
        System.out.println("1: Prior menu");
        int choice = -1;
        while (choice != 1) {
            try {
                System.out.print("Select an option: ");
                choice = Integer.parseInt(userIn.next());
            } catch (java.lang.NumberFormatException e){}
        }

    }

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

package pdm.h2.demo;

import pdm.h2.demo.objects.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Driver class for the Dealer's login.
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
                if ((option > 0) && (option <= 3))
                    break;
                System.out.print("Please input a number from the list above: ");
            }

            switch (option) {
                case 1:
                    System.out.println("\n--VEHICLE INVENTORY--");
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
                    System.out.println("\n Goodbye!");
                    break;



            }


        }


    }

    private static void viewInventory(H2DemoMain demo, Dealer currentDealer) {

        ArrayList<Vehicle> vehicleInventory = new ArrayList<Vehicle>();

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
            for (int i = 0; i < vehicleInventory.size();i++){
                System.out.println(i + 1 + ": " + vehicleInventory.get(i).getYear() + " " + vehicleInventory.get(i).getMake() + " " + vehicleInventory.get(i).getModel());
            }
            int choice = -1;


            System.out.println("\n-----Car Selection-----");
            while (true) {

                System.out.print("Pick a car to view more information: ");
                String input = userIn.next();


                try {
                    choice = Integer.parseInt(input);
                } catch (java.lang.NumberFormatException e){}

                if (choice > 0 && choice <= vehicleInventory.size()) {
                    showVehicleInformation(vehicleInventory.get(choice - 1));
                    break;
                }
                else {
                    System.out.println("Not a valid option.");
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    private static void viewCustomers(H2DemoMain demo, Dealer currentDealer) {
        //TODO
    }

    private static void viewManufacturers(H2DemoMain demo, Dealer currentDealer) {
        //TODO
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
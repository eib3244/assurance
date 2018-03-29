package pdm.h2.demo;

import pdm.h2.demo.objects.DealerSale;
import pdm.h2.demo.objects.DealerVehicleInventory;

import javax.tools.JavaFileManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main Driver for our DataBase
 */
public class H2DemoMain {

	//The connection to the database
	private Connection conn;
	
	/**
	 * Create a database connection with the given params
	 * @param location: path of where to place the database
	 * @param user: user name for the owner of the database
	 * @param password: password of the database owner
	 */
	public void createConnection(String location, String user, String password){
		try {
			String url = "jdbc:h2:" + location;
			
			// tells it to use the h2 driver
			Class.forName("org.h2.Driver");
			
			//creates the connection
			conn = DriverManager.getConnection(url,
					                           user,
					                           password);
		} catch (SQLException | ClassNotFoundException e) {
			//You should handle this better
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the connection
	 * @return: returns class level connection
	 */
	public Connection getConnection(){
		return conn;
	}
	
	/*
	 * Used to close the connection when the database exits
	 */
	public void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts and runs the database
	 * @param args: not used but you can use them
	 */
	public static void main(String[] args) {
		
		H2DemoMain demo = new H2DemoMain();

		// used for testing. allows us to run the program without having to delete the database.
		try {
            java.nio.file.Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/test.mv.db"));
        } catch (java.io.IOException e){ System.out.println("Error: " + e); }

		// Hard drive location of the database.
		// We put it in the current working directory
		String location = System.getProperty("user.dir") + "/test";
		System.out.println(location);
		String user = "scj";
		String password = "password";
		
		//Create the database connections, basically makes the database
		demo.createConnection(location, user, password);


// used to load our customer data into the database
// Note if database has been created we will error out as it ties to add customers who are already in the database
// if we don't delete the database file at the start
		try{
			CustomerTable.createCustomerTable(demo.getConnection());
			CustomerTable.populateCustomerTableFromCSV(demo.getConnection(),"1_Customer_Data.csv");
            CustomerTable.addCustomer(demo.getConnection(),"000-00-0000","juan remirez #1", "Male", 25000, 10, "South Park", "WestHempstead" , "NY", "11552","juan1@aol.com","000001" );
            CustomerTable.addCustomer(demo.getConnection(),"000-00-0000","juan remirez #2", "Male", 25000, 10, "South Park", "WestHempstead" , "NY", "11552","juan1@aol.com","000000" );
         	//   System.out.println("/////\nCustomer Table: \n/////");
           	// CustomerTable.printCustomerTable(demo.getConnection());
		}catch (SQLException e) {e.printStackTrace();}

// used to load customer phone#'s into the database
        try{
            CustomerPhoneTable.createCustomerPhoneTable(demo.getConnection());
            CustomerPhoneTable.populateCustomerPhoneTableFromCSV(demo.getConnection(),"2_Customer_Phone_Data.csv");
            CustomerPhoneTable.addPhoneNumber(demo.getConnection(),"000-00-0000","315-222-9999");
            CustomerPhoneTable.addPhoneNumber(demo.getConnection(),"000-00-0000","315-222-8888");
         //   System.out.println("/////\nCustomer Phone Table: \n/////");
           // CustomerPhoneTable.printCustomerPhoneTable(demo.getConnection());
        }catch (SQLException e) {e.printStackTrace();}

// used to load customer sales into the database
        try{
		    CustomerSaleTable.createCustomerSaleTable(demo.getConnection());
		    CustomerSaleTable.populateCustomerSaleTableFromCSV(demo.getConnection(), "3_Customer_Sales_Data.csv");
		   // CustomerSaleTable.printCustomerTable(demo.getConnection());
        }catch (SQLException e) {e.printStackTrace();}

// used to load dealers into the database
		try{
			DealerTable.createDealerTable(demo.getConnection());
			DealerTable.populateDealerTableFromCSV(demo.getConnection(), "6_Dealer_Data.csv");
			//DealerTable.printDealerTable(demo.getConnection());
		}catch (SQLException e) {e.printStackTrace();}

// used to load dealer sales into the database
	try{
		DealerSaleTable.createDealerSaleTable(demo.getConnection());
		DealerSaleTable.populateDealerSaleTableFromCSV(demo.getConnection(), "7_Dealer_Sale_Data.csv");
		//DealerSaleTable.printDealerSaleTable(demo.getConnection());
	}catch (SQLException e) {e.printStackTrace();}


// used to load Dealer Inventory into the database
	try{
		DealerVehicleInventoryTable.createDealerVehicleInventoryTable(demo.getConnection());
		DealerVehicleInventoryTable.populateDealerVehicleInventoryTableFromCSV(demo.getConnection(), "8_Dealer_Vehicle_Inventory_Data.csv");
		//DealerVehicleInventoryTable.printCustomerTable(demo.getConnection());
	}catch (SQLException e) {e.printStackTrace();}

// used to load Manufacturers into our database
	try{
		ManufacturerTable.createManufacturerTable(demo.getConnection());
		ManufacturerTable.populateManufacturerTableFromCSV(demo.getConnection(), "9_Manufacturer_Data.csv");
		//ManufacturerTable.printDealerTable(demo.getConnection());
	}catch (SQLException e) {e.printStackTrace();}

// used to load Manufacturer Vehicles into the database
	try{
			ManufacturerVehiclesTable.createManufacturerVehiclesTable(demo.getConnection());
		ManufacturerVehiclesTable.populateDealerVehicleInventoryTableFromCSV(demo.getConnection(), "10_Manufacturer_Vehicle_Data.csv");
		//ManufacturerVehiclesTable.printCustomerTable(demo.getConnection());
	}catch (SQLException e) {e.printStackTrace();}

// used to load vehicles sold to customers into the database
	try{
		VehiclesSoldToCustomerTable.createVehiclesSoldToCustomerTable(demo.getConnection());
		VehiclesSoldToCustomerTable.populateVehiclesSoldToCustomerTableFromCSV(demo.getConnection(), "4_Vehicles_Sold_To_Customers_Data.csv");
		//VehiclesSoldToCustomerTable.printVehiclesSoldToCustomerTable(demo.getConnection());
	}catch (SQLException e) {e.printStackTrace();}

// used to load vehicles sold to dealers into the database
		try{
			VehiclesSoldToDealersTable.createVehiclesSoldToDealersTable(demo.getConnection());
			VehiclesSoldToDealersTable.populateVehiclesSoldToDealersTableFromCSV(demo.getConnection(), "7_Vehicles_Sold_To_Dealers.csv");
			//VehiclesSoldToDealersTable.printVehiclesSoldToDealerTable(demo.getConnection());
		}catch (SQLException e) {e.printStackTrace();}
// used to load vehicles into the database
	try{
		VehicleTable.createVehicleTable(demo.getConnection());
		VehicleTable.populateVehiclesTableFromCSV(demo.getConnection(),"5_Vehicle_Data.csv" );
		//VehicleTable.printVehiclesTable(demo.getConnection());
	}catch (SQLException e) {e.printStackTrace();}

	// simple login example
		// use juan1@aol.com and 000001 or 000000 to test
		// you should get juan #1 or #2

	userLoginMain.main(null);

	}
}

package pdm.h2.demo;

import pdm.h2.demo.objects.cls;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

/**
 * Main Driver for our DataBase
 * Author: Emerson Bolha
 */
public class H2DemoMain {
	private static Scanner userin = new Scanner(System.in);
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

		// Hard drive location of the database.
		// We put it in the current working directory
		String location = System.getProperty("user.dir") + "/test";
		String user = "scj";
		String password = "password";
		
		//Create the database connections, basically makes the database

 // loads database with csv files if we cant find it
if (!java.nio.file.Files.exists(Paths.get(System.getProperty("user.dir") + "/test.mv.db"))) {
	demo.createConnection(location, user, password);

	System.out.println("file not found creating tables and loading data.....");
// mass creation of tables
	VehicleTable.createVehicleTable(demo.getConnection());

	CustomerTable.createCustomerTable(demo.getConnection());
	CustomerPhoneTable.createCustomerPhoneTable(demo.getConnection());

	DealerTable.createDealerTable(demo.getConnection());

	ManufacturerTable.createManufacturerTable(demo.getConnection());
	ManufacturerVehiclesTable.createManufacturerVehiclesTable(demo.getConnection());

	DealerVehicleInventoryTable.createDealerVehicleInventoryTable(demo.getConnection());
	DealerSaleTable.createDealerSaleTable(demo.getConnection());
	VehiclesSoldToDealersTable.createVehiclesSoldToDealersTable(demo.getConnection());

	CustomerSaleTable.createCustomerSaleTable(demo.getConnection());
	VehiclesSoldToCustomerTable.createVehiclesSoldToCustomerTable(demo.getConnection());


// used to load our customer data into the database
// Note if database has been created we will error out as it ties to add customers who are already in the database
// if we don't delete the database file at the start
	try {
		CustomerTable.populateCustomerTableFromCSV(demo.getConnection(), "1_Customer_Data.csv");
		CustomerTable.addCustomer(demo.getConnection(), "000-00-0001", "juan remirez #1", "Male", 25000, 10, "South Park", "WestHempstead", "NY", "11552", "juan@aol.com", "0");
		CustomerTable.addCustomer(demo.getConnection(), "000-00-0002", "juan remirez #2", "Male", 25000, 10, "South Park", "WestHempstead", "NY", "11552", "juan@aol.com", "0");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load customer phone#'s into the database
	try {
		CustomerPhoneTable.populateCustomerPhoneTableFromCSV(demo.getConnection(), "2_Customer_Phone_Data.csv");
		CustomerPhoneTable.addPhoneNumber(demo.getConnection(), "000-00-0001", "315-222-9999");
		CustomerPhoneTable.addPhoneNumber(demo.getConnection(), "000-00-0001", "315-222-8888");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load vehicles into the database
	try {
		VehicleTable.populateVehiclesTableFromCSV(demo.getConnection(), "5_Vehicle_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load dealers into the database
	try {
		DealerTable.populateDealerTableFromCSV(demo.getConnection(), "6_Dealer_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load Manufacturers into our database
	try {
		ManufacturerTable.populateManufacturerTableFromCSV(demo.getConnection(), "9_Manufacturer_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load Manufacturer Vehicles into the database
	try {
		ManufacturerVehiclesTable.populateDealerVehicleInventoryTableFromCSV(demo.getConnection(), "10_Manufacturer_Vehicle_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load Dealer Inventory into the database
	try {
		DealerVehicleInventoryTable.populateDealerVehicleInventoryTableFromCSV(demo.getConnection(), "8_Dealer_Vehicle_Inventory_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load dealer sales into the database
	try {
		DealerSaleTable.populateDealerSaleTableFromCSV(demo.getConnection(), "7_Dealer_Sale_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load vehicles sold to dealers into the database
	try {
		VehiclesSoldToDealersTable.populateVehiclesSoldToDealersTableFromCSV(demo.getConnection(), "7_Vehicles_Sold_To_Dealers.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load customer sales into the database
	try {
		CustomerSaleTable.populateCustomerSaleTableFromCSV(demo.getConnection(), "3_Customer_Sales_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}

// used to load vehicles sold to customers into the database
	try {
		VehiclesSoldToCustomerTable.populateVehiclesSoldToCustomerTableFromCSV(demo.getConnection(), "4_Vehicles_Sold_To_Customers_Data.csv");
	} catch (SQLException e) {
		e.printStackTrace();
	}


	CustomerSaleTable.addCustomerSale(demo.getConnection(), "0000000", "000-00-0001", "crcg6", System.currentTimeMillis(), 62331);

	VehiclesSoldToCustomerTable.addVehicleSoldToCustomer(demo.getConnection(), "0000000", "r2nfcgeaeogutxub2");
	VehiclesSoldToCustomerTable.addVehicleSoldToCustomer(demo.getConnection(), "0000000", "qebqow2w7l52nss1x");

	DealerVehicleInventoryTable.removeVehicleFromDealerInventory(demo.getConnection(), "r2nfcgeaeogutxub2");
	DealerVehicleInventoryTable.removeVehicleFromDealerInventory(demo.getConnection(), "qebqow2w7l52nss1x");

}
else
	demo.createConnection(location, user, password);

	// launch user login
        System.out.println("1: Customer Login");
        System.out.println("2: Dealer Login");
        System.out.println("3: Admin Login");
		System.out.println("4: Log Off");
		System.out.print("Select an option: ");

		int choice = -1;
		while(true) {

			String userinput= userin.next();

			try{
				choice = Integer.parseInt(userinput);
			} catch (java.lang.NumberFormatException e){}

			if (choice < 1 || choice > 4){
				System.out.print("\nPlease enter a number from the list above: ");
			}
			else
				break;
		}

		switch (choice) {
			case(1):
				cls.clear();
				userLoginMain.main(null);
				break;

			case(2):
				cls.clear();
				dealerLoginMain.main(null);
				break;

			case(3):
				// Run a java app in a separate system process
				Process proc = null;
				try {
					proc = Runtime.getRuntime().exec("java -jar h2-1.4.197.jar");
				} catch (IOException e) {
					e.printStackTrace();
				}
				demo.closeConnection();
				break;

			case 4:
				cls.clear();
				System.out.println("Goodbye!");
				break;
		}
	}
}

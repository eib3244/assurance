package pdm.h2.demo;

import pdm.h2.demo.objects.ManufacturerVehicle;
import pdm.h2.demo.objects.VehicleSoldToCustomer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VehiclesSoldToCustomerTable {
    public static void createVehiclesSoldToCustomerTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS vehicles_sold_to_customer("
                    + "Sale_ID VARCHAR(7),"
                    + "VIN VARCHAR (17),"
                    + "PRIMARY KEY(Sale_ID, VIN)"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void populateVehiclesSoldToCustomerTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<VehicleSoldToCustomer> VehiclesSoldToCustomer = new ArrayList<VehicleSoldToCustomer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                VehiclesSoldToCustomer.add(new VehicleSoldToCustomer(split));
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createVehiclesSoldToCustomerInsertSQL(VehiclesSoldToCustomer);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createVehiclesSoldToCustomerInsertSQL(ArrayList<VehicleSoldToCustomer> VehiclesSoldToCustomer){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO vehicles_sold_to_customer " +
                "(Sale_ID, VIN) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < VehiclesSoldToCustomer.size(); i++){
            VehicleSoldToCustomer p = VehiclesSoldToCustomer.get(i);
            sb.append(String.format("(\'%s\', \'%s\')",
                    p.getSale_ID(), p.getVIN()));
            if( i != VehiclesSoldToCustomer.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static void addVehicleSoldToCustomer(Connection conn, String sale_id, String vin){
        String query = String.format("INSERT INTO vehicles_sold_to_customer "
                        + "VALUES (\'%s\',\'%s\');",
                sale_id, vin);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void printVehiclesSoldToCustomerTable(Connection conn){
        String query = "SELECT * FROM vehicles_sold_to_customer;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Sale_ID: %s VIN: %s \n",
                        result.getString(1),
                        result.getString(2));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}

package pdm.h2.demo;

import pdm.h2.demo.objects.ManufacturerVehicle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * Author: Emerson Bolha
 */
public class ManufacturerVehiclesTable {

    public static void createManufacturerVehiclesTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS ManufacturerVehicles("
                    + "M_ID VARCHAR(8) NOT NULL,"
                    + "VIN VARCHAR(17) NOT NULL,"
                    +"PRIMARY KEY(M_ID, VIN),"
                    + "UNIQUE (M_ID, VIN),"
                    +"FOREIGN KEY (VIN) REFERENCES vehicles(VIN)"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void populateDealerVehicleInventoryTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<ManufacturerVehicle> ManufacturerInventory = new ArrayList<ManufacturerVehicle>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                ManufacturerInventory.add(new ManufacturerVehicle(split));
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createManufacturerVehicleInventoryInsertSQL(ManufacturerInventory);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createManufacturerVehicleInventoryInsertSQL(ArrayList<ManufacturerVehicle> ManufacturerInventory){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO ManufacturerVehicles " +
                "(M_ID, VIN) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < ManufacturerInventory.size(); i++){
            ManufacturerVehicle p = ManufacturerInventory.get(i);
            sb.append(String.format("(\'%s\', \'%s\')",
                   p.getM_ID(), p.getVIN()));
            if( i != ManufacturerInventory.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static void addManufacturerVehicles(Connection conn, String m_id, String vin){
        String query = String.format("INSERT INTO ManufacturerVehicles "
                        + "VALUES (\'%s\',\'%s\');",
                m_id, vin);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void removeVehicleFromManufacturerInventory(Connection conn, String VIN) {
        String query = String.format("DELETE FROM MANUFACTURERVEHICLES WHERE "
        + "VIN=\'%s\';", VIN);

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printCustomerTable(Connection conn){
        String query = "SELECT * FROM ManufacturerVehicles;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("MID: %s VIN: %s \n",
                        result.getString(1),
                        result.getString(2));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}

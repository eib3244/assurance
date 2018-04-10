package pdm.h2.demo;

import pdm.h2.demo.objects.VehicleSoldToDealer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VehiclesSoldToDealersTable {

    public static void createVehiclesSoldToDealersTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS VehiclesSoldToDealers("
                    + "Dealer_Sale_ID VARCHAR(7) NOT NULL,"
                    + "VIN VARCHAR(17) NOT NULL,"
                    + "PRIMARY KEY(Dealer_Sale_ID, VIN),"
                    + "UNIQUE (Dealer_Sale_ID, VIN),"
                    +"FOREIGN KEY (Dealer_Sale_ID) REFERENCES Dealer_Sale(Dealer_Sale_ID),"
                    +"FOREIGN KEY (VIN) REFERENCES vehicles(VIN)"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void populateVehiclesSoldToDealersTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<VehicleSoldToDealer> VehiclesSoldToDealer = new ArrayList<VehicleSoldToDealer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                VehiclesSoldToDealer.add(new VehicleSoldToDealer(split));
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createVehiclesSoldToDealerInsertSQL(VehiclesSoldToDealer);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createVehiclesSoldToDealerInsertSQL(ArrayList<VehicleSoldToDealer> VehiclesSoldToDealer){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO VehiclesSoldToDealers " +
                "(Dealer_Sale_ID, VIN) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < VehiclesSoldToDealer.size(); i++){
            VehicleSoldToDealer p = VehiclesSoldToDealer.get(i);
            sb.append(String.format("(\'%s\', \'%s\')",
                    p.getDealer_Sale_ID(), p.getVIN()));
            if( i != VehiclesSoldToDealer.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static void addVehiclesSoldToDealers(Connection conn, String dealer_s_id, String vin){
        String query = String.format("INSERT INTO VehiclesSoldToDealers "
                        + "VALUES (\'%s\',\'%s\');",
                dealer_s_id, vin);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void printVehiclesSoldToDealerTable(Connection conn){
        String query = "SELECT * FROM VehiclesSoldToDealers;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("D_S_ID: %s VIN: %s \n",
                        result.getString(1),
                        result.getString(2));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}

package pdm.h2.demo;

import pdm.h2.demo.objects.Vehicle;
import pdm.h2.demo.objects.VehicleSoldToCustomer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VehicleTable {
    public static void createVehicleTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS vehicles("
                    + "VIN VARCHAR(17) PRIMARY KEY,"
                    + "Make VARCHAR(20),"
                    + "Model VARCHAR(30),"
                    + "Brand VARCHAR(20),"
                    + "Year INT,"
                    + "Engine VARCHAR(10),"
                    + "Color VARCHAR(20),"
                    + "Transmission VARCHAR(10),"
                    + "Drive_Type VARCHAR(3),"
                    + "Price INT,"
                    + "Miles INT"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void populateVehiclesTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Vehicle> Vehicles = new ArrayList<Vehicle>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                Vehicles.add(new Vehicle(split));
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createVehiclesInsertSQL(Vehicles);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createVehiclesInsertSQL(ArrayList<Vehicle> Vehicles){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO vehicles " +
                "(VIN, Make, Model, Brand, Year, Engine, Color, Transmission, Drive_Type, Price, Miles) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < Vehicles.size(); i++){
            Vehicle p = Vehicles.get(i);
            sb.append(String.format("(\'%s\', \'%s\', \'%s\', \'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\', %d, %d)",
                   p.getVIN(), p.getMake(), p.getModel(), p.getBrand(), p.getYear(), p.getEngine(), p.getColor(), p.getTransmission(), p.getDrive_Type(), p.getPrice(), p.getMiles() ));
            if( i != Vehicles.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static void addVehicle(Connection conn,String VIN,String Make,String Model,String Brand,int Year,String Engine,String Color, String Transmission, String Drive_Type, int Price,int Miles){
        String query = String.format("INSERT INTO vehicles "
                        + "VALUES (\'%s\', \'%s\', \'%s\', \'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\', %d, %d);",
                VIN, Make, Model, Brand, Year, Engine, Color, Transmission, Drive_Type, Price, Miles);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void printVehiclesTable(Connection conn){
        String query = "SELECT * FROM vehicles;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("VIN: %s %s %s %s $%d %d \n",
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(9),
                        result.getInt(10),
                        result.getInt(11));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}

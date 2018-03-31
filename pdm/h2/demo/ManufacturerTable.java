package pdm.h2.demo;

import pdm.h2.demo.objects.Dealer;
import pdm.h2.demo.objects.Manufacturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ManufacturerTable {

    public static void createManufacturerTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS Manufacturer("
                    + "M_ID VARCHAR(8) NOT NULL PRIMARY KEY,"
                    + "Name VARCHAR(30) NOT NULL,"
                    + "UNIQUE (M_ID)"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void populateManufacturerTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Manufacturer> Manufacturers = new ArrayList<Manufacturer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                Manufacturers.add(new Manufacturer(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createManufacturerInsertSQL(Manufacturers);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createManufacturerInsertSQL(ArrayList<Manufacturer> Manufacturers){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO Manufacturer " +
                "(M_ID, Name) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < Manufacturers.size(); i++){
            Manufacturer p = Manufacturers.get(i);
            sb.append(String.format("(\'%s\', \'%s\')",
                   p.getM_ID(), p.getName()));
            if( i != Manufacturers.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static void addManufacturer(Connection conn, String m_id, String name){
        String query = String.format("INSERT INTO Manufacturer "
                        + "VALUES (%s,\'%s\');",
                m_id, name);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void printDealerTable(Connection conn){
        String query = "SELECT * FROM Manufacturer;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Manufacturer %s: %s\n",
                        result.getString(1),
                        result.getString(2));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}

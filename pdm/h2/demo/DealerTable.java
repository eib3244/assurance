package pdm.h2.demo;

import pdm.h2.demo.objects.Dealer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DealerTable {

    public static void createDealerTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS dealer("
                    + "Dealer_ID VARCHAR(5) PRIMARY KEY,"

                    // !!!!!!! NO ' in name !
                    + "Name VARCHAR(40),"
                    + "Street_Number INT,"
                    + "Street VARCHAR(50),"
                    + "City VARCHAR(20),"
                    + "State VARCHAR(2),"
                    + "Zip VARCHAR(5),"
                    + "Phone_num VARCHAR(12)"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void populateDealerTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Dealer> Dealers = new ArrayList<Dealer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                Dealers.add(new Dealer(split));
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
        String sql = createDealerInsertSQL(Dealers);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }


    public static String createDealerInsertSQL(ArrayList<Dealer> Dealers){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO dealer " +
                "(Dealer_ID, Name, Street_Number, Street, City, State, Zip, Phone_Num) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < Dealers.size(); i++){
            Dealer p = Dealers.get(i);
            sb.append(String.format("(\'%s\', \'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\', \'%s\')",
                    p.getDealer_ID(), p.getName(), p.getStreet_Number(), p.getStreet(), p.getCity(), p.getState(), p.getZip(), p.getPhone_Num()));
            if( i != Dealers.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }



    public static void addDealer(Connection conn, String dealer_id, String name,
                                     int street_num, String street,
                                     String city, String state, String zip, String phone_num){
        String query = String.format("INSERT INTO dealer "
                        + "VALUES (\'%s\',\'%s\',%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%s\');",
                dealer_id, name, street_num, street, city, state, zip, phone_num);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void printDealerTable(Connection conn){
        String query = "SELECT * FROM dealer;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Dealer %s: %s %d %s %s %s %s %s\n",
                        result.getString(1),
                        result.getString(2),
                        result.getInt(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6),
                        result.getString(7),
                        result.getString(8));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

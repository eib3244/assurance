package pdm.h2.demo;

import pdm.h2.demo.objects.CustomerPhone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerPhoneTable {
	
    public static void createCustomerPhoneTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS customer_phone("
                    + "SSN VARCHAR(11),"
                    + "Phone_Num VARCHAR(12),"
                    // composite primary key
                    + "PRIMARY KEY (SSN, Phone_Num),"
                    +"FOREIGN KEY (SSN) REFERENCES customer(SSN)"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace(); }
    }

	 public static void populateCustomerPhoneTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<CustomerPhone> customerPhones = new ArrayList<CustomerPhone>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                customerPhones.add(new CustomerPhone(split));
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createCustomerPhoneInsertSQL(customerPhones);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createCustomerPhoneInsertSQL(ArrayList<CustomerPhone> customerPhones){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO customer_phone " +
                "(SSN, Phone_Num) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < customerPhones.size(); i++){
            CustomerPhone p = customerPhones.get(i);
            sb.append(String.format("(\'%s\',\'%s\')",
                    p.getSSN(), p.getPhoneNumber()));
            if( i != customerPhones.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }
	
    public static void addPhoneNumber(Connection conn, String ssn, String phone_num){
        String query = String.format("INSERT INTO customer_phone "
                        + "VALUES (\'%s\',\'%s\');",
                ssn, phone_num);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }
	
	public static void printCustomerPhoneTable(Connection conn){
        String query = "SELECT * FROM customer_phone;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Customer %s: %s\n",
                        result.getString(1),
                        result.getString(2));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }
	
}
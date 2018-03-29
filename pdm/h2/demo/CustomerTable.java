package pdm.h2.demo;

import pdm.h2.demo.objects.Customer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerTable {
//TODO make SSN UNIQUE along with OTHER prom keys in other tables
    //TODO ADD foreign keys to tables !


    public static void createCustomerTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS customer("
                    + "SSN VARCHAR(11) PRIMARY KEY,"
                    + "Name VARCHAR(50),"
                    + "Gender VARCHAR(6),"
                    + "Income INT,"
                    + "House_Num INT,"
                    + "Street VARCHAR(50),"
                    + "City VARCHAR(20),"
                    + "State VARCHAR(2),"
                    + "ZIP VARCHAR(5),"
                    + "Email VARCHAR(40),"
                    + "Password VARCHAR(20)"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void populateCustomerTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Customer> customers = new ArrayList<Customer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                customers.add(new Customer(split));
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
        String sql = createCustomerInsertSQL(customers);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createCustomerInsertSQL(ArrayList<Customer> customers){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO customer " +
                "(SSN, Name, Gender, Income, House_num, Street, City, State, ZIP, Email, Password) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < customers.size(); i++){
            Customer p = customers.get(i);
            sb.append(String.format("(\'%s\',\'%s\',\'%s\',%d,%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\')",
                    p.getSSN(),p.getName(),p.getGender(),p.getIncome(),p.gethouse_num(),p.getStreet(),
                    p.getCity(),p.getState(),p.getZip(),p.getEmail(),p.getPassword()));
            if( i != customers.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }


    public static void addCustomer(Connection conn, String ssn, String name,
                                 String gender, int income,
                                 int house_num, String street,
                                 String city, String state,
                                 String zip, String email,
                                 String password){
        String query = String.format("INSERT INTO customer "
                        + "VALUES (\'%s\',\'%s\',\'%s\',%d,%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\');",
                ssn, name, gender, income, house_num, street, city, state, zip, email, password);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){
            //e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    public static void printCustomerTable(Connection conn){
        String query = "SELECT * FROM customer;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Customer %s: %s %s %d\n",
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}



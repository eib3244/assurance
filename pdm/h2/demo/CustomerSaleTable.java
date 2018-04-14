package pdm.h2.demo;


import pdm.h2.demo.objects.CustomerSale;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

// !!!!!! Date Must be a LONG !
public class CustomerSaleTable {
    public static void createCustomerSaleTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS customer_sale_table("
                    + "Sale_ID VARCHAR(7) NOT NULL PRIMARY KEY,"
                    + "SSN VARCHAR (11) NOT NULL,"
                    + "Dealer_ID VARCHAR(5) NOT NULL,"
                    + "Date Long NOT NULL,"
                    + "Total INT NOT NULL,"
                    + "UNIQUE (Sale_ID),"
                    +"FOREIGN KEY (SSN) REFERENCES customer(SSN),"
                    +"FOREIGN KEY (Dealer_ID) REFERENCES dealer(Dealer_ID)"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void populateCustomerSaleTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<CustomerSale> CustomerSales = new ArrayList<CustomerSale>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                CustomerSales.add(new CustomerSale(split));
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
        String sql = createCustomerSalesInsertSQL(CustomerSales);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createCustomerSalesInsertSQL(ArrayList<CustomerSale> CustomerSales){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO customer_sale_table " +
                "(Sale_ID, SSN, Dealer_ID, Date, Total) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < CustomerSales.size(); i++){
            CustomerSale p = CustomerSales.get(i);
            sb.append(String.format("(\'%s\', \'%s\', \'%s\', %d, %d)",
                    p.getSaleID(), p.getSSN(), p.getDealer_ID(), p.getDate(), p.getTotal()));
            if( i != CustomerSales.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }

    public static void addCustomerSale(Connection conn, String sale_id, String ssn, String dealer_id, long date, int total){
        String query = String.format("INSERT INTO customer_sale_table "
                        + "VALUES (\'%s\', \'%s\', \'%s\', %d, %d);",
                        sale_id, ssn, dealer_id, date, total);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void printCustomerTable(Connection conn){
        String query = "SELECT * FROM customer_sale_table;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                Date d = new Date(result.getLong(4));
                System.out.printf("Sale %s: %s %s %tF $%d\n",
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        d,
                        result.getInt(5));
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}
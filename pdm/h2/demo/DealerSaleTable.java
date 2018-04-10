package pdm.h2.demo;

import pdm.h2.demo.objects.CustomerSale;
import pdm.h2.demo.objects.DealerSale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class DealerSaleTable {
    public static void createDealerSaleTable(Connection conn){
        try{
            String query = "CREATE TABLE IF NOT EXISTS Dealer_Sale("
                    + "Dealer_Sale_ID VARCHAR(7) NOT NULL PRIMARY KEY,"
                    + "Dealer_ID VARCHAR(5) NOT NULL,"
                    + "M_ID VARCHAR(8) NOT NULL,"
                    + "Date LONG NOT NULL,"
                    + "Total INT NOT NULL,"
                    + "UNIQUE (Dealer_Sale_ID),"
                    + "FOREIGN KEY (Dealer_ID) REFERENCES dealer(Dealer_ID),"
                    + "FOREIGN KEY (M_ID) REFERENCES Manufacturer(M_ID)"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void populateDealerSaleTableFromCSV(Connection conn, String fileName) throws SQLException{

        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<DealerSale> DealerSales = new ArrayList<DealerSale>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                DealerSales.add(new DealerSale(split));
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createDealerSalesInsertSQL(DealerSales);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static String createDealerSalesInsertSQL(ArrayList<DealerSale> DealerSales){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO Dealer_Sale " +
                "(Dealer_Sale_ID, Dealer_ID, M_ID, Date, Total) VALUES");

        /**
         * For each person append a (id, first_name, last_name, MI) tuple
         *
         * If it is not the last person add a comma to seperate
         *
         * If it is the last person add a semi-colon to end the statement
         */
        for(int i = 0; i < DealerSales.size(); i++){
            DealerSale p = DealerSales.get(i);
            sb.append(String.format("(\'%s\', \'%s\', \'%s\', %d, %d)",
                    p.getDealer_Sale_ID(), p.getDealer_ID(), p.getM_ID(), p.getDate(), p.getTotal()));
            if( i != DealerSales.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static void addDealerSale(Connection conn, String dealer_sale_id, String dealer_id,
                                 String m_id, Long date,
                                 int total){
        String query = String.format("INSERT INTO Dealer_Sale "
                        //+ "VALUES (\'s%\', \'s%\', \'s%\', %d, %d);",
                         + "VALUES (\'%s\', \'%s\', \'%s\', %d, %d);",
                dealer_sale_id, dealer_id, m_id, date, total);
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void printDealerSaleTable(Connection conn){
        String query = "SELECT * FROM Dealer_Sale;";
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

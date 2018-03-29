package pdm.h2.demo;

import pdm.h2.demo.objects.DealerSale;
import pdm.h2.demo.objects.DealerVehicleInventory;

import javax.tools.JavaFileManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class userLoginMain {


    public static void main(String[] args) {

        H2DemoMain demo = new H2DemoMain();

        String location = System.getProperty("user.dir") + "/test";
        String user = "scj";
        String password = "password";

        //Create the database connections, basically makes the database
        demo.createConnection(location, user, password);


        Scanner userin = new Scanner(System.in);
        System.out.println("Enter in a email:");
        String email = userin.nextLine();
        System.out.println("Enter in a password:");
        String passwordLogin = userin.nextLine();

        String query = "SELECT * FROM customer WHERE Email=\'" + email + "\' AND Password=\'" + passwordLogin + "\';";

        System.out.println(query);
        try {
            Statement stmt =  demo.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(query);

            // need .next !!
            result.next();
            System.out.printf("Customer %s: %s %s %d\n",
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

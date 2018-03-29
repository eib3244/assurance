package pdm.h2.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class exampleQueries {

//       try {

//       /**
//        * Creates a sample Person table
//        * and populates it from a csv file
//        */
//       PersonTable.createPersonTable(demo.getConnection());
//       PersonTable.populatePersonTableFromCSV(
//               demo.getConnection(),
//               "people.csv");
//       //C:/Users/eib3244/Downloads/H2Demo/
//       /**
//        * Just displays the table
//        */
//       PersonTable.printPersonTable(demo.getConnection());

//       /**
//        * Runs a basic query on the table
//        */
//       System.out.println("\n\nPrint results of SELECT * FROM person");
//       ArrayList<String> whereStmnt = new ArrayList<>();

//       // example of selecting emerson's only.
//       Scanner userin = new Scanner(System.in);

//       System.out.println("Enter a name: ");
//       String input = userin.nextLine();
//       whereStmnt.add("first_name = \'" + input + "\' ");

//       ResultSet results = PersonTable.queryPersonTable(
//               demo.getConnection(),
//               new ArrayList<String>(),
//               whereStmnt);

//       /**
//        * Iterates the Result set
//        *
//        * Result Set is what a query in H2 returns
//        *
//        * Note the columns are not 0 indexed
//        * If you give no columns it will return them in the
//        * order you created them. To gaurantee order list the columns
//        * you want
//        */
//       while(results.next()){
//           System.out.printf("\tPerson %d: %s %s %s\n",
//                   results.getInt(1),
//                   results.getString(2),
//                   results.getString(4),
//                   results.getString(3));
//       }

//       /**
//        * A more complex query with columns selected and
//        * addition conditions
//        */
//       System.out.println("\n\nPrint results of SELECT "
//               + "id, first_name "
//               + "FROM person "
//               + "WHERE first_name = \'Scott\' "
//               + "AND last_name = \'Johnson\'");

//       /**
//        * This is one way to do this, but not the only
//        *
//        * Create lists to make the whole thing more generic or
//        * you can just construct the whole query here
//        */
//       ArrayList<String> columns = new ArrayList<String>();
//       columns.add("id");
//       columns.add("first_name");
//       columns.add("last_name");

//       /**
//        * Conditionals
//        */
//       ArrayList<String> whereClauses = new ArrayList<String>();
//       whereClauses.add("first_name = \'Scott\'");
//       whereClauses.add("last_name = \'Johnson\'");

//       /**
//        * query and get the result set
//        *
//        * parse the result set and print it
//        *
//        * Notice not all of the columns are here because
//        * we limited what to show in the query
//        */
//       ResultSet results2 = PersonTable.queryPersonTable(
//               demo.getConnection(),
//               columns,
//               whereClauses);
//       while(results2.next()){
//           System.out.printf("\tPerson %d: %s %s\n",
//                   results2.getInt(1),
//                   results2.getString(2),
//                   results2.getString(3));
//       }
//   } catch (SQLException e) {
//       e.printStackTrace();
//   }
//
}


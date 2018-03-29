package pdm.h2.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pdm.h2.demo.objects.Person;

/**
 * Class to make and manipulate the person table
 * @author scj
 *
 */
public class PersonTable {

	/**
	 * Reads a cvs file for data and adds them to the person table
	 * 
	 * Does not create the table. It must already be created
	 * 
	 * @param conn: database connection to work with
	 * @param fileName
	 * @throws SQLException
	 */
	public static void populatePersonTableFromCSV(Connection conn, String fileName) throws SQLException{

		/**
		 * Structure to store the data as you read it in
		 * Will be used later to populate the table
		 * 
		 * You can do the reading and adding to the table in one
		 * step, I just broke it up for example reasons
		 */
		ArrayList<Person> people = new ArrayList<Person>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				people.add(new Person(split));
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
		String sql = createPersonInsertSQL(people);
		
		/**
		 * Create and execute an SQL statement
		 * 
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
	    stmt.execute(sql);
	}
	
	/**
	 * Create the person table with the given attributes
	 * 
	 * @param conn: the database connection to work with
	 */
	public static void createPersonTable(Connection conn){
		try {
			String query = "CREATE TABLE IF NOT EXISTS person("
					     + "ID INT PRIMARY KEY,"
					     + "FIRST_NAME VARCHAR(255),"
					     + "LAST_NAME VARCHAR(255),"
					     + "MI VARCHAR(1),"
					     + ");" ;
			
			/**
			 * Create a query and execute
			 */
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a single person to the database
	 * 
	 * @param conn
	 * @param id
	 * @param fName
	 * @param lName
	 * @param MI
	 */
	public static void addPerson(Connection conn,
			                     int id,
			                     String fName,
			                     String lName,
			                     String MI){
		
		/**
		 * SQL insert statement
		 */
		String query = String.format("INSERT INTO person "
				                   + "VALUES(%d,\'%s\',\'%s\',\'%s\');",
				                     id, fName, lName, MI);
		try {
			/**
			 * create and execute the query
			 */
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This creates an sql statement to do a bulk add of people
	 * 
	 * @param people: list of Person objects to add
	 * 
	 * @return
	 */
	public static String createPersonInsertSQL(ArrayList<Person> people){
		StringBuilder sb = new StringBuilder();
		
		/**
		 * The start of the statement, 
		 * tells it the table to add it to
		 * the order of the data in reference 
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO person (id, FIRST_NAME, LAST_NAME, MI) VALUES");
		
		/**
		 * For each person append a (id, first_name, last_name, MI) tuple
		 * 
		 * If it is not the last person add a comma to seperate
		 * 
		 * If it is the last person add a semi-colon to end the statement
		 */
		for(int i = 0; i < people.size(); i++){
			Person p = people.get(i);
			sb.append(String.format("(%d,\'%s\',\'%s\',\'%s\')", 
					p.getId(), p.getFirstName(), p.getLastName(), p.getMI()));
			if( i != people.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Makes a query to the person table 
	 * with given columns and conditions
	 * 
	 * @param conn
	 * @param columns: columns to return
	 * @param whereClauses: conditions to limit query by
	 * @return
	 */
	public static ResultSet queryPersonTable(Connection conn,
			                                 ArrayList<String> columns,
			                                 ArrayList<String> whereClauses){
		StringBuilder sb = new StringBuilder();
		
		/**
		 * Start the select query
		 */
		sb.append("SELECT ");
		
		/**
		 * If we gave no columns just give them all to us
		 * 
		 * other wise add the columns to the query
		 * adding a comma top seperate
		 */
		if(columns.isEmpty()){
			sb.append("* ");
		}
		else{
			for(int i = 0; i < columns.size(); i++){
				if(i != columns.size() - 1){
				    sb.append(columns.get(i) + ", ");
				}
				else{
					sb.append(columns.get(i) + " ");
				}
			}
		}
		
		/**
		 * Tells it which table to get the data from
		 */
		sb.append("FROM person ");
		
		/**
		 * If we gave it conditions append them
		 * place an AND between them
		 */
		if(!whereClauses.isEmpty()){
			sb.append("WHERE ");
			for(int i = 0; i < whereClauses.size(); i++){
				if(i != whereClauses.size() -1){
					sb.append(whereClauses.get(i) + " AND ");
				}
				else{
					sb.append(whereClauses.get(i));
				}
			}
		}
		
		/**
		 * close with semi-colon
		 */
		sb.append(";");
		
		//Print it out to verify it made it right
		System.out.println("Query: " + sb.toString());
		try {
			/**
			 * Execute the query and return the result set
			 */
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Queries and print the table
	 * @param conn
	 */
	public static void printPersonTable(Connection conn){
		String query = "SELECT * FROM person;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("Person %d: %s %s %s\n",
						          result.getInt(1),
						          result.getString(2),
						          result.getString(4),
						          result.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}

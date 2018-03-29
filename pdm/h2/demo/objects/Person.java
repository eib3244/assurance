package pdm.h2.demo.objects;

/**
 * Hold data about a person
 * @author scj
 *
 */
public class Person {

	String fName;
	String lName;
	String MI;
	
	int id;
	
	public Person(int id,
			      String fName,
			      String lName,
			      String MI){
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.MI = MI;
	}
	
	public Person(String[] data){
		this.id = Integer.parseInt(data[0]);
		this.fName = data[1];
		this.lName = data[2];
		this.MI = data[3];
	}

	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return fName;
	}
	
	public String getLastName() {
		return lName;
	}
	
	public String getMI() {
		return MI;
	}
}

package pdm.h2.demo.objects;

public class Customer {
    String SSN;
    String Name;
    String Gender;
    int Income;
    int house_num;
    String Street;
    String City;
    String State;
    String Zip;
    String Email;
    String Password;

    public Customer(String SSN,
                    String Name,
                    String Gender,
                    int Income,
                    int house_num,
                    String Street,
                    String City,
                    String State,
                    String Zip,
                    String Email,
                    String Password){

        this.SSN= SSN;
        this.Name= Name;
        this.Gender= Gender;
        this.Income= Income;
        this.house_num= house_num;
        this.Street= Street;
        this.City= City;
        this.State= State;
        this.Zip= Zip;
        this.Email= Email;
        this.Password = Password;
    }

    public Customer(String[] data){
        this.SSN= data[0];
        this.Name= data[1];
        this.Gender= data[2];
        this.Income= Integer.parseInt(data[3]);
        this.house_num= Integer.parseInt(data[4]);
        this.Street= data[5];
        this.City= data[6];
        this.State= data[7];
        this.Zip= data[8];
        this.Email= data[9];
        this.Password = data[10];
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getIncome() {
        return Income;
    }

    public void setIncome(int income) {
        Income = income;
    }

    public int gethouse_num() {
        return house_num;
    }

    public void sethouse_num(int house_num) {
        house_num = house_num;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

package pdm.h2.demo.objects;

public class Dealer {
    String Dealer_ID;
    String Name;
    int Street_Number;
    String Street;
    String City;
    String State;
    String Zip;
    String Phone_Num;

    public Dealer(String Dealer_ID, String Name, int Street_Number, String Street, String City, String State, String Zip, String Phone_Num){
        this.Dealer_ID = Dealer_ID;
        this.Name = Name;
        this.Street_Number = Street_Number;
        this.Street = Street;
        this.City = City;
        this.State = State;
        this.Zip = Zip;
        this.Phone_Num = Phone_Num;
    }

    public Dealer(String[] data){
        this.Dealer_ID = data[0];
        this.Name = data[1];
        this.Street_Number = Integer.parseInt(data[2]);
        this.Street = data[3];
        this.City = data[4];
        this.State = data[5];
        this.Zip = data[6];
        this.Phone_Num = data[7];
    }

    public String getDealer_ID() {
        return Dealer_ID;
    }

    public void setDealer_ID(String dealer_ID) {
        Dealer_ID = dealer_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStreet_Number() {
        return Street_Number;
    }

    public void setStreet_Number(int street_Number) {
        Street_Number = street_Number;
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

    public String getPhone_Num() {
        return Phone_Num;
    }

    public void setPhone_Num(String phone_Num) {
        Phone_Num = phone_Num;
    }

}

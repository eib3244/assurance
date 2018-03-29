package pdm.h2.demo.objects;

public class Vehicle {
    String VIN;
    String Make;
    String Model;
    String Brand;
    int Year;
    String Engine;
    String Color;
    String Transmission;
    String Drive_Type;
    int Price;
    int Miles;

    public Vehicle(String VIN,String Make,String Model,String Brand,int Year,String Engine,String Color, String Transmission, String Drive_Type, int Price,int Miles){
        this.VIN = VIN;
        this.Make = Make;
        this.Model = Model;
        this.Brand = Brand;
        this.Year = Year;
        this.Engine = Engine;
        this.Color = Color;
        this.Transmission = Transmission;
        this.Drive_Type = Drive_Type;
        this.Price = Price;
        this.Miles = Miles;
    }

    public Vehicle(String data[]){
        this.VIN = data[0];
        this.Make = data[1];
        this.Model = data[2];
        this.Brand = data[3];
        this.Year = Integer.parseInt(data[4]);
        this.Engine = data[5];
        this.Color = data[6];
        this.Transmission = data[7];
        this.Drive_Type = data[8];
        this.Price = Integer.parseInt(data[9]);
        this.Miles = Integer.parseInt(data[10]);
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getTransmission() {
        return Transmission;
    }

    public void setTransmission(String transmission) {
        Transmission = transmission;
    }

    public String getDrive_Type() {
        return Drive_Type;
    }

    public void setDrive_Type(String drive_Type) {
        Drive_Type = drive_Type;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getMiles() {
        return Miles;
    }

    public void setMiles(int miles) {
        Miles = miles;
    }

}

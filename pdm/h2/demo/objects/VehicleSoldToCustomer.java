package pdm.h2.demo.objects;

public class VehicleSoldToCustomer {
    String Sale_ID;
    String VIN;

    public VehicleSoldToCustomer(String Sale_ID, String VIN){
        this.Sale_ID = Sale_ID;
        this.VIN = VIN;
    }

    public VehicleSoldToCustomer(String data[]){
        this.Sale_ID = data[0];
        this.VIN = data[1];
    }

    public String getSale_ID() {
        return Sale_ID;
    }

    public void setSale_ID(String sale_ID) {
        Sale_ID = sale_ID;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

}

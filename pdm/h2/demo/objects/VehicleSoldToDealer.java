package pdm.h2.demo.objects;

public class VehicleSoldToDealer {
    String Dealer_Sale_ID;
    String VIN;

    public VehicleSoldToDealer(String Dealer_Sale_ID, String VIN){
        this.Dealer_Sale_ID = Dealer_Sale_ID;
        this.VIN = VIN;
    }

    public VehicleSoldToDealer(String data[]){
        this.Dealer_Sale_ID = data[0];
        this.VIN = data[1];
    }

    public String getDealer_Sale_ID() {
        return Dealer_Sale_ID;
    }

    public void setDealer_Sale_ID(String dealer_Sale_ID) {
        Dealer_Sale_ID = dealer_Sale_ID;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

}

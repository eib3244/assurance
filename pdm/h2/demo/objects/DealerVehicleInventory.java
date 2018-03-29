package pdm.h2.demo.objects;

public class DealerVehicleInventory{
    String Dealer_ID;
    String VIN;

    public DealerVehicleInventory(String Dealer_ID, String VIN){
        this.Dealer_ID = Dealer_ID;
        this.VIN = VIN;
    }

    public DealerVehicleInventory(String data[]){
        this.Dealer_ID = data[0];
        this.VIN = data[1];
    }

    public String getDealer_ID() {
        return Dealer_ID;
    }

    public void setDealer_ID(String dealer_ID) {
        Dealer_ID = dealer_ID;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

}

package pdm.h2.demo.objects;

public class ManufacturerVehicle {
    String M_ID;
    String VIN;

    public ManufacturerVehicle(String M_ID, String VIN){
        this.M_ID = M_ID;
        this.VIN = VIN;
    }

    public ManufacturerVehicle(String data[]){
        this.M_ID = data[0];
        this.VIN = data[1];
    }

    public String getM_ID() {
        return M_ID;
    }

    public void setM_ID(String m_ID) {
        M_ID = m_ID;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

}

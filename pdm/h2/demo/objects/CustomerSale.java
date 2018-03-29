package pdm.h2.demo.objects;

public class CustomerSale {
    String SaleID;
    String SSN;
    String Dealer_ID;
    Long Date;
    int Total;

    CustomerSale(String SaleID,  String SSN, String Dealer_ID, Long Date, int Total){
        this.SaleID = SaleID;
        this.SSN = SSN;
        this.Dealer_ID = Dealer_ID;
        this.Date = Date;
        this.Total = Total;
    }

    public CustomerSale(String[] data){
        this.SaleID = data[0];
        this.SSN = data[1];
        this.Dealer_ID = data[2];
        this.Date = Long.parseLong(data[3]);
        this.Total = Integer.parseInt(data[4]);
    }

    public String getSaleID() {
        return SaleID;
    }

    public void setSaleID(String saleID) {
        SaleID = saleID;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getDealer_ID() {
        return Dealer_ID;
    }

    public void setDealer_ID(String dealer_ID) {
        Dealer_ID = dealer_ID;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

}

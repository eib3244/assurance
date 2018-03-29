package pdm.h2.demo.objects;

public class DealerSale {
    String Dealer_Sale_ID;
    String Dealer_ID;
    String M_ID;
    Long Date;
    Integer Total;

    public DealerSale(String Dealer_Sale_ID,String Dealer_ID,String M_ID,Long Date,Integer Total){
        this.Dealer_Sale_ID = Dealer_Sale_ID;
        this.Dealer_ID = Dealer_ID;
        this.M_ID = M_ID;
        this.Date = Date;
        this.Total = Total;
    }

    public DealerSale(String[] data){
        this.Dealer_Sale_ID = data[0];
        this.Dealer_ID = data[1];
        this.M_ID = data[2];
        this.Date = Long.parseLong(data[3]);
        this.Total = Integer.parseInt(data[4]);
    }

    public String getDealer_Sale_ID() {
        return Dealer_Sale_ID;
    }

    public void setDealer_Sale_ID(String dealer_Sale_ID) {
        Dealer_Sale_ID = dealer_Sale_ID;
    }

    public String getDealer_ID() {
        return Dealer_ID;
    }

    public void setDealer_ID(String dealer_ID) {
        Dealer_ID = dealer_ID;
    }

    public String getM_ID() {
        return M_ID;
    }

    public void setM_ID(String m_ID) {
        M_ID = m_ID;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }

    public Integer getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

}

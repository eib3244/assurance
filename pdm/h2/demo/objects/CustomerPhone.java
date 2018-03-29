package pdm.h2.demo.objects;

public class CustomerPhone {
    String PhoneNumber;
    String SSN;

    public CustomerPhone(String PhoneNumber, String SSN){
        this.PhoneNumber = PhoneNumber;
        this.SSN = SSN;
    }

    public CustomerPhone(String[] data){
        this.SSN= data[0];
        this.PhoneNumber= data[1];
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }
}

package pdm.h2.demo.objects;

public class Manufacturer {
    String M_ID;
    String Name;

    public Manufacturer(String M_ID, String Name){
        this.M_ID = M_ID;
        this.Name = Name;
    }

    public Manufacturer(String data[]){
        this.M_ID = data[0];
        this.Name = data[1];
    }

    public String getM_ID() {
        return M_ID;
    }

    public void setM_ID(String m_ID) {
        M_ID = m_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}

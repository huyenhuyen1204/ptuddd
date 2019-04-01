package ie.app.models;

public class Donation {
    public int amount;
    public String method;
    public int id;

    public Donation(int amount, String method){
        this.amount = amount;
        this.method = method;
    }
}

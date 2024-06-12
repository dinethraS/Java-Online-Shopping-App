import java.io.Serializable;

public class Product implements Serializable {
    private String productID;
    private String productName;
    private int noOfAvailableItems;
    private double price;

    public Product(String productID, String productName, int noOfAvailableItems, double price) {
        this.productID = productID;
        this.productName = productName;
        this.noOfAvailableItems = noOfAvailableItems;
        this.price = price;
    }
//Getter and Setters for Product
    public String getProductID() {return productID;}
    public String getProductName() {return productName;}
    public int getNoOfAvailableItems() {return noOfAvailableItems;}
    public double getPrice() {return price;}

    public void setNoOfAvailableItems(int noOfAvailableItems) {
        this.noOfAvailableItems = noOfAvailableItems;
    }

    public String getInfo() {
        return "Not Available";
    }


}

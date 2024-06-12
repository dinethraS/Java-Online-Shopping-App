public class Clothing extends Product  {
    private String size;

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    private String color;

    public Clothing(String productID, String productName, int noOfAvailableItems, double price, String size, String color) {
        super(productID, productName, noOfAvailableItems, price);
        this.size = size;
        this.color = color;
    }

    public void printC(){
        String value = ("ProductID: "+ super.getProductID() + "\nProduct Name: "+ super.getProductName()
                + "\nNo. of Available Items: " + super.getNoOfAvailableItems() + "\nPrice: "
                + super.getPrice() + "\nSize: "+ size + "\nColor: " + color );
        System.out.println(value);
    }
@Override
    public String getInfo(){
        return ("Size: " + size+" ,"+"Color: "+color);
    }
}

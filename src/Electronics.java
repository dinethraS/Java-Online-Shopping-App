public class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;

    public Electronics(String productID, String productName, int noOfAvailableItems, double price, String brand, int warrantyPeriod) {
        super(productID, productName, noOfAvailableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }
    public void printE(){
        String value =("ProductID: "+ super.getProductID() + "\nProduct Name: "+ super.getProductName()
                + "\nNo. of Available Items: " + super.getNoOfAvailableItems() + "\nPrice: "
                + super.getPrice() +  "\nBrand: " + brand + "\nWarranty Period (In Years): "+ warrantyPeriod );
        System.out.println(value);
    }
@Override
    public String getInfo(){
        return ("Brand: " + brand+" ,"+"Warranty Period: "+warrantyPeriod);
    }
}

import java.io.*;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager, Serializable {
    private static ArrayList<Product> availableProducts = new ArrayList<>();
    static int productCount = 0;
    @Override
    public void displayConsoleMenu() {//Menu displayed in console

        System.out.println("""
                    
                    CONSOLE MENU OPTIONS
                                
                1) ADD Products
                2) DELETE Products
                3) PRINT a list of Products
                4) SAVE
                       
                    PRESS "0" To EXIT
                                
                """);
    }
    @Override
    public void addProduct() {
        Validate verifyAdd = new Validate();
        //to check if the maximum limit is reached
        if (productCount == 50) {
            System.out.println("Maximum limit has been added!!");
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.println("""
                    
                    ADD PRODUCTS
                    """);
            int i = 0;
            //Get validated input using method in class Validate
            int count = verifyAdd.validateInput(sc, "Enter the number of new products you want to add : ", Integer.class);
            //User can decide how many products to add without extra prompt
            Product product;
            while (true) {
                String productType;
                while (true) {
                    productType = verifyAdd.validateInput(sc, """
                            
                            Clothing or Electronics
                            Press "c" if Clothing
                            Press "e" if Electronics
                            Product Type: """, String.class);
                    if (!(productType.equals("c") || productType.equals("e"))) {//Validating correct options are selected
                        System.out.println("Invalid Option");
                    } else {
                        break;
                    }
                }
                String productID;
                while (true){
                    productID = verifyAdd.validateInput(sc, "Product ID: ", String.class);
                    String checkChar = String.valueOf(productID.charAt(0));
                    if (productID.length() < 4) {
                        System.out.println("Product ID must be at least 4 characters long!");
                    } else if(productType.toLowerCase().equals("e")&&(!checkChar.equals("A") ||Character.isDigit(productID.charAt(0)))){
                        System.out.println("Please start ID with 'A'");
                    }else if(productType.toLowerCase().equals("c")&&(!checkChar.equals("B") ||Character.isDigit(productID.charAt(0)))){
                        System.out.println("Please start ID with 'B'");
                    }else {
                        break;
                    }

                }
                //Check if product is already added. method is in class Validate
                boolean value = verifyAdd.isAvailable(productID);
                if (value) {
                    System.out.println("Product is already in the system!!");
                    //Checking if user wants to keep adding products
                    String choice = verifyAdd.YesNo("add");
                    if (choice.equals("n")) {
                        break;
                    }
                } else {
                    String productName = verifyAdd.validateInput(sc, "Product name : ", String.class);
                    int noOfAvailableItems = verifyAdd.validateInput(sc, "No. of Available Items : ", Integer.class);
                    double price = verifyAdd.validateInput(sc, "Price : ", Double.class);
                    switch (productType) {
                        case "c" -> {//To get data from subclasses
                            String size;
                            while (true) {
                                size = verifyAdd.validateInput(sc, "Size (xs,s,m,l,xl,xxl) : ", String.class);
                                if (size.matches("xs|s|m|l|xl|xxl")){
                                    break;
                                }else{
                                    System.out.println("Invalid size. Please choose from: xs, s, m, l, xl, xxl");
                                }
                            }//unable to add an entry that is not included here
                            String color = verifyAdd.validateInput(sc, "Color : ", String.class);
                            product = new Clothing(productID, productName, noOfAvailableItems, price, size, color);
                        }
                        case "e" -> {
                            String brand = verifyAdd.validateInput(sc, "Brand of Electronic : ", String.class);
                            int warrantyPeriod = verifyAdd.validateInput(sc, "Warranty Period (In years) : ", Integer.class);
                            product = new Electronics(productID, productName, noOfAvailableItems, price, brand, warrantyPeriod);
                        }
                        default -> product = null;
                    }

                    productCount++; //increment count. Maximum is 50
                    availableProducts.add(product);//product added to ArrayList
                    System.out.println("Added Successfully!");
                    i++;//to check if number of products user wanted to add is done
                    if (i >= count) {
                        String choice = verifyAdd.YesNo("add");//User can add more if wants or can exit
                        if (choice.toLowerCase().equals("n")) {
                            break;
                        }
                    }
                }

            }
        }
        System.out.println("Number of products in the system" + " " + productCount);
    }


    @Override
    public void deleteProduct() {
        Validate verifyDelete = new Validate();
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                
                DELETE PRODUCTS
                """);
        //Getting input using a method that already validates it
        int count = verifyDelete.validateInput(sc, "Enter the number of products you want to delete :", Integer.class);
        int i = 0;
        while (true) {
            String productID = verifyDelete.validateInput(sc, "Enter Product ID : ", String.class);
            // Using a method to check if product is in the system using a method in class Validate.
            boolean value = verifyDelete.isAvailable(productID);
            if (!value) {
                System.out.println("This Product unavailable!");
                //Using a method to ask if user wants to delete another product
                String choice = verifyDelete.YesNo("delete");
                if (choice.toLowerCase().equals("n")) {
                    break;
                }
            } else {
                // checking if product is available to delete.
                for (Product product : availableProducts) {
                    if (product.getProductID().equals(productID)) {
                        availableProducts.remove(product);
                        productCount--;//Reduce product count
                        System.out.printf("Deleted %s " + productID + " " + "successfully!\n", product instanceof Electronics ? "Electronics" : "Clothing");
                        break;
                    }
                }
                i++;
                //If count is equal or greater the number of products user wanted to delete
                if (i >=count) {//if y will loop again
                    String choice = verifyDelete.YesNo("delete");
                    if (choice.toLowerCase().equals("n")) {
                        break;
                    }
                }

            }
        }
        System.out.println("Number of products in the system" + " " + productCount);
    }
    @Override
    public void printProducts() {
        if (availableProducts.isEmpty()) {//checking if there are products available
            System.out.println("There are no Products!!");
        } else {
            //Sorting the products alphabetically by product id.
            availableProducts.sort(Comparator.comparing(Product::getProductID));
            System.out.println("""
                    
                    PRODUCT LIST
                    """);
            for (Product product : availableProducts) {
                if (product instanceof Clothing) {
                    System.out.println("\nClothing\n");
                    ((Clothing) product).printC();//method in Clothing class
                } else if (product instanceof Electronics) {
                    System.out.println("\n\nElectronics\n");
                    ((Electronics) product).printE();//method in Electronics class
                }
            }
        }
    }

    @Override
    public void saveProducts() {
        try {
            // Create a FileOutputStream to write to "product.txt"
            FileOutputStream fileOutputStream = new FileOutputStream("product.txt");
            // Create an ObjectOutputStream to serialize objects into the file
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            // Write the ArrayList of availableProducts to the file
            objectOutputStream.writeObject(availableProducts);
            // Close the ObjectOutputStream and FileOutputStream
            objectOutputStream.close();
            fileOutputStream.close();

            System.out.println("Product Information is Saved");
        } catch (IOException e) {
            // Handle any IOException that may occur during the save process
            System.out.println("Attempt to save is unsuccessful! An error has occurred.");
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<Product> load() {
        try {
            // Create a FileInputStream to read from "product.txt"
            FileInputStream fileInputStream = new FileInputStream("product.txt");
            // Create an ObjectInputStream to deserialize objects from the file
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            // Read the ArrayList of availableProducts from the file
            availableProducts = (ArrayList<Product>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

        } catch (FileNotFoundException ignored) {// Handle any IOException that may occur during the save process
        } catch (Exception e) {
            System.out.println("Error Loading File");
        }
        return availableProducts;
    }

    @Override
    public ArrayList<Product> getAvailableProducts() {
        return availableProducts;
    }




}




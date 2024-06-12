import java.util.List;
import java.util.Scanner;

public class Validate {
    final static Scanner sc = new Scanner(System.in);

    public boolean isAvailable(String productID){//To check if product is already added to array
        boolean value= false;
        ShoppingManager manager = new WestminsterShoppingManager();
        List<Product> availableProducts = manager.getAvailableProducts();//get array of products
        if (!(availableProducts == null)) {//will only check if there are products already
            for (Product product : availableProducts) {
                if (product.getProductID().equals(productID)) {
                    value = true;
                    break;
                }
            }
        }
        return value;

    }

    public String YesNo(String action){
        while (true){
            String choice;
            // Prompt the user with the provided action
            System.out.println(" Do you want to " + action + " more products?");
            choice = sc.next();
            // Check if the choice is "y" or "n" (case-insensitive)
            if (choice.equalsIgnoreCase("y")|| choice.equalsIgnoreCase("n")){
                return choice;
            }else{
                System.out.println("Invalid Option!!");
            }

        }
    }



    public <T> T validateInput(Scanner sc, String prompt, Class<T> type) {
        T input = null;
        // Loop until valid input is provided
        while (input == null) {
            try {
                System.out.print(prompt);
                // Read the user input as a string
                String userInput = sc.nextLine();
                if (type == Integer.class||type == Double.class) {
                    // If the input type is Integer or Double, perform additional validation
                    int value = Integer.parseInt(userInput);
                    if (value == 0) {
                        throw new IllegalArgumentException();
                    }
                }
                // create an instance of the specified type from the user input
                input = type.cast(type.getConstructor(String.class).newInstance(userInput));
            } catch (Exception e) {
                // Catch any exception that may occur during input validation
                System.out.println("Invalid input. Please enter a valid " + type.getSimpleName() + ".");
            }
        }
        return input;
    }

}

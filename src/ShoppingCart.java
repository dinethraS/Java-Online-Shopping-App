import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCart {
    private ArrayList<Product> cartItems;
    private HashMap<String, Integer> ItemQuantity = new HashMap<>();


    public ShoppingCart() {
        cartItems = new ArrayList<>();
    }

    public ArrayList<Product> getCartItems() {
        return cartItems;
    }

    public void addToCart(Product product, JFrame frame) {
        String key = product.getProductID();
        // checking if product is already added.
        if (ItemQuantity.containsKey(key)) {
            int currentQuantity = ItemQuantity.get(key);
            //Checking if there is sufficient items available
            if (product.getNoOfAvailableItems()<1) {
                //pop up message
                JOptionPane.showMessageDialog(null, "Not enough items available!!" , "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Object not added to cart but quantity is increased.
            ItemQuantity.put(key, currentQuantity + 1);
            String message = product.getProductName() + " added to the cart.";
            JOptionPane.showMessageDialog(frame, message, "Product Added", JOptionPane.INFORMATION_MESSAGE);
            product.setNoOfAvailableItems(product.getNoOfAvailableItems()-1);

        } else {
            //object added to both cart and quantity is increased
            cartItems.add(product);
            ItemQuantity.put(key, 1);
            String message = product.getProductName() + " added to the cart.";
            JOptionPane.showMessageDialog(frame, message, "Product Added", JOptionPane.INFORMATION_MESSAGE);
            product.setNoOfAvailableItems(product.getNoOfAvailableItems()-1);
        }
    }



    public int getQuantity(String key){
        return ItemQuantity.get(key);
    }
}

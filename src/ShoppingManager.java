import java.util.ArrayList;

public interface ShoppingManager {
    void displayConsoleMenu();
    void addProduct();
    ArrayList<Product> getAvailableProducts();
    void deleteProduct();
    void printProducts();
    void saveProducts();
    ArrayList<Product> load();
}

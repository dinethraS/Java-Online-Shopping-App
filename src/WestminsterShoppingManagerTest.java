import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class WestminsterShoppingManagerTest {

    @Test
    public void testAddProduct_validClothing() {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();

        // Prepare dummy user input as a String
        String userInput = """
                1
                c
                B1234
                Test Clothing
                10
                15.99
                m
                blue""";

        // Redirect System.in to a stream containing the dummy input
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        manager.addProduct();

        // Reset System.in
        System.setIn(originalIn);

        // Assert that the product was added correctly
        ArrayList<Product> products = manager.getAvailableProducts();
        assertEquals(1, products.size());
        Product addedProduct = products.get(0);
        assertEquals("B1234", addedProduct.getProductID());
        assertEquals("Test Clothing", addedProduct.getProductName());
        assertEquals(10, addedProduct.getNoOfAvailableItems());
        assertEquals(15.99, addedProduct.getPrice());
        assertTrue(addedProduct instanceof Clothing);
        Clothing clothing = (Clothing) addedProduct;
        assertEquals("m", clothing.getSize());
        assertEquals("blue", clothing.getColor());
    }

    @Test
    public void testDeleteProduct_existingProduct() {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();

        // Add a product to delete
        String addProductInput = """
                1
                c
                B1234
                Test Clothing
                10
                15.99
                m
                blue""";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(addProductInput.getBytes()));
        manager.addProduct();
        System.setIn(originalIn);

        // Mock inputs for deleting the product
        String deleteProductInput = "B1234\n" +
                "y";
        System.setIn(new ByteArrayInputStream(deleteProductInput.getBytes()));

        manager.deleteProduct();

        // Reset System.in
        System.setIn(originalIn);

        // Assert that the product was deleted
        ArrayList<Product> products = manager.getAvailableProducts();
        assertEquals(0, products.size());
    }

//    @Test
//    public void testPrintProducts_emptyList() {
//        WestminsterShoppingManager manager = new WestminsterShoppingManager();
//
//        // Capture the output to a string
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        manager.printProducts();
//
//        String expectedOutput = "There are no Products!!\n";
//
//        // Reset System.out
//        System.setOut(System.out);
//
//        assertEquals(expectedOutput, outContent.toString());
//    }
    @Test
    public void testSaveProducts_successfulSave() throws IOException {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();

        // Add some products to save
        String addProductInput = """
                1
                c
                B1234
                Test Clothing
                10
                15.99
                m
                blue""";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(addProductInput.getBytes()));
        manager.addProduct();
        System.setIn(originalIn);

        manager.saveProducts();

        // Assert that the file was created and contains the expected content
        FileReader fileReader = new FileReader("products.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        assertEquals("B1234,Test Clothing,10,15.99,m,blue", line); // Assuming product data is saved as a single comma-separated line
        bufferedReader.close();
    }
}


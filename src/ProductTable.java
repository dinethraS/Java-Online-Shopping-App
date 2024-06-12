import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Currency;

public class ProductTable extends AbstractTableModel {
    private static final ShoppingManager manager = new WestminsterShoppingManager();
    private ArrayList<Product> tableArray = manager.load();//Loading product details
    private ArrayList<Product> filteredArray = new ArrayList<>(tableArray);;
    private Currency pound = Currency.getInstance("EUR");//Needed for the pound sign
    private String[] fields = {"Product ID","Category","Name","Price (" +pound.getSymbol() + ")","Info"};

    public ProductTable(){

    }

    @Override
    public int getRowCount() {
        return filteredArray.size();
    }

    @Override
    public int getColumnCount() {
        return fields.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {//
        Product product = filteredArray.get(rowIndex);
        switch (columnIndex){
            case 0: return product.getProductID();
            case 1: return(product instanceof Electronics? "Electronics":"Clothing" );
            case 2: return product.getProductName();
            case 3: return product.getPrice();
            case 4: return product.getInfo();
            default: return null;
        }
    }

    public void filterProducts(String selectedCategory) {
        filteredArray.clear();
        if ("All".equals(selectedCategory)) {
            filteredArray.addAll(tableArray);
        } else {
            // Filter products based on the selected category
            for (Product product : tableArray) {
                if ((product instanceof Electronics && "Electronics".equals(selectedCategory))
                        || (product instanceof Clothing && "Clothing".equals(selectedCategory))) {
                    filteredArray.add(product);
                }
            }
        }
        fireTableDataChanged();
    }



    @Override
    public String getColumnName(int column) {
        return fields[column];
    }

    public ArrayList<Product> getFilteredArray() {
        return filteredArray;
    }

    public ArrayList<Product> getTableArray() {
        return tableArray;
    }
}

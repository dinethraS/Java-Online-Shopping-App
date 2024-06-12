import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Currency;

public class MainFrame extends JFrame{
    private final JFrame frame;
    private JFrame cartFrame;
    private static JPanel topPanel, centerPanel, bottomPanel, top, bottom;
    private static JTable productTable;
    private ProductTable tableInfoGet = new ProductTable();
    private ShoppingCart cart = new ShoppingCart();
    private Currency pound = Currency.getInstance("EUR");



    public MainFrame(){
        //Create main frame
        frame = new JFrame("Westminster Shopping Center");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Divide frame into panel
        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(0, 150));
        topPanel.setLayout(new BorderLayout());

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(0,325));
        bottomPanel.setLayout(new BorderLayout());

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        ViewCartButton();
        createProductTable();
        createDropdownMenu();

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new MainFrame();
    }

    private void createDropdownMenu() {
        JPanel topLabel = new JPanel(new FlowLayout(FlowLayout.LEFT,30,25));
        topLabel.add(new JLabel("Select Product Category:"));

        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER,0,25));
        //Use array to get combo box options
        String[] productTypes = {"All", "Electronics","Clothing"};
        JComboBox<String> menu = new JComboBox<>(productTypes);
        menu.setPreferredSize(new Dimension(200, 30));
        menu.addItemListener(e -> {
            if (e.getStateChange()==ItemEvent.SELECTED){
                String selectedCategory = (String) e.getItem();
                //Use productModel table to access method that filters array
                tableInfoGet.filterProducts(selectedCategory);
            }
        });
        topMenu.add(topLabel);
        topMenu.add(menu);
        topPanel.add(topMenu,BorderLayout.CENTER);

    }

    private void createProductTable() {
        productTable = new JTable(tableInfoGet);
        productTable.setRowHeight(40);
        //Can sort tables by clicking header
        DefaultRowSorter<TableModel,Integer> sorter = new TableRowSorter<>(tableInfoGet);
        productTable.setRowSorter(sorter);
        //Use a custom renderer to make wanted rows red
        for (int i = 0; i < productTable.getColumnCount(); i++) {  // Set the renderer for all columns
            productTable.getColumnModel().getColumn(i).setCellRenderer(new MakeRed(tableInfoGet));
        }

        JScrollPane scrollTable = new JScrollPane(productTable);
        JPanel TablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTableHeader header = productTable.getTableHeader();
        //Change cell size of table heading row
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        scrollTable.getViewport().setPreferredSize(new Dimension(900, 150));
        scrollTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        TablePanel.add(scrollTable);
        centerPanel.add(TablePanel, BorderLayout.CENTER);
        centerPanel.revalidate();

        //Make rows selectable
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow != -1) {
                        //getting values needed for product details with selected row
                        String pID = (String) productTable.getValueAt(selectedRow, 0);
                        String pCategory = (String) productTable.getValueAt(selectedRow, 1);
                        String pName = (String) productTable.getValueAt(selectedRow, 2);
                        String pInfo = (String) productTable.getValueAt(selectedRow, 4);
                        //Make visible when selected
                        addProductDetails(pID, pCategory, pName, pInfo);
                        AddToCartButton();

                    }


                }
            }
        });
    }

    public class MakeRed extends DefaultTableCellRenderer {
        private final ProductTable productTableModel;

        public MakeRed(ProductTable productTableModel) {
            this.productTableModel = productTableModel;
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component rowData = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Will retrieve the product in filteredArray at index row
            Product product = productTableModel.getFilteredArray().get(row);

            if (product != null) {
                if (product.getNoOfAvailableItems() < 3) {
                    //make text red
                    rowData.setForeground(Color.RED);
                } else if (!isSelected) {
                    //setting text color same as table text color
                    rowData.setForeground((table.getForeground()));
                }
            }

            return rowData;
        }
    }
    private void AddToCartButton() {
        JPanel bottomButt = new JPanel(new FlowLayout(FlowLayout.CENTER,0,25));
        JButton addToCartBtn = new JButton("Add to Shopping Cart");

        addToCartBtn.addActionListener(e -> {
            // get index of selected row
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                // Will retrieve the product in filteredArray at index selected row
                Product selectedProduct = tableInfoGet.getFilteredArray().get(selectedRow);
                cart.addToCart(selectedProduct, frame);//selected Product is sent to add cart method which will add if valid
            }
        });

        bottomButt.add(addToCartBtn);
        bottomPanel.add(bottomButt,BorderLayout.SOUTH);

    }

    private void addProductDetails(String pID, String pCategory, String pName, String pInfo) {
        bottomPanel.removeAll(); // Clear existing components

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical arrangement

        JLabel titleLabel = new JLabel("Selected Product - Details");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD)); // Make title bold
        detailsPanel.add(titleLabel);

        detailsPanel.add(Box.createVerticalStrut(25)); // Add vertical spacing

        //labels are created using a custom method
        addDetailLabel(detailsPanel, "Product ID: ", pID);
        addDetailLabel(detailsPanel, "Category: ", pCategory);
        addDetailLabel(detailsPanel, "Name: ", pName);
        //method to send info into an array and get the 2 component of data separately.
        splitInfoDetails(pInfo,detailsPanel);

        ArrayList<Product> itemsSearch = tableInfoGet.getTableArray();
        for (Product p : itemsSearch){
            //Checking for product info using selected row pID
            if (p.getProductID() == pID) {
                //Get no of items using p and use custom label method to display
                addDetailLabel(detailsPanel, "No Of Items Available : ",String.valueOf(p.getNoOfAvailableItems()));
            }
        }

        detailsPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 10, 10));
        //add horizontal ruler
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(separator, BorderLayout.NORTH);
        bottomPanel.add(detailsPanel, BorderLayout.WEST);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    private void addDetailLabel(JPanel panel, String labelText, String data) {//custom
        JLabel label = new JLabel(labelText + " "+data);
        label.setHorizontalAlignment(SwingConstants.RIGHT); // Align labels to the right
        panel.add(label);
        panel.add(Box.createVerticalStrut(15)); // Add vertical spacing between labels
    }

    private void splitInfoDetails(String info, JPanel panel) {//method to split info column data in 2 components using arrays
        String[] infoArray = info.split(",");
        JLabel label1 = new JLabel(infoArray[0]);
        panel.add(label1);//
        panel.add(Box.createVerticalStrut(15));
        JLabel label2 = new JLabel(infoArray[1]);
        panel.add(label2);
        panel.add(Box.createVerticalStrut(15));
    }

    private void ViewCartButton() {

        JPanel topCart = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,20));
        JButton cart = new JButton("Shopping Cart");

        cart.addActionListener(e -> {
            showShoppingCartFrame();
        });

        topCart.add(cart);
        topPanel.add(topCart,BorderLayout.EAST);

    }

    private void showShoppingCartFrame() {
        cartFrame = new JFrame("Shopping Cart");
        cartFrame.setSize(800, 600);

        //Call on these methods when frame is created
        createShoppingCartTable();
        showReceipt();
        // Set the default close operation to dispose the frame when closed
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cartFrame.setVisible(true);
    }

    private DefaultTableModel createShoppingCartTable() {
        ArrayList<Product> cartItems = cart.getCartItems();

        //Use Default Table Model to create a table
        DefaultTableModel shoppingCartTable = new DefaultTableModel();
        //Table Headers are set using add column
        shoppingCartTable.addColumn("Product");
        shoppingCartTable.addColumn("Quantity");
        shoppingCartTable.addColumn("Price ("+(pound.getSymbol())+")");

        for (Product product : cartItems) {
            //For each item in cartItems arrayList display following in columns consecutively
            String proInfo = (product.getProductID() + " , "+ product.getProductName() +" , "+ product.getInfo());
            shoppingCartTable.addRow(new Object[]{
                    proInfo,
                    cart.getQuantity(product.getProductID()),
                    product.getPrice() * cart.getQuantity(product.getProductID())
            });
        }

        top = new JPanel();
        bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(0,300));

        JTable cartTable = new JTable(shoppingCartTable);
        cartTable.setRowHeight(50);


        JTableHeader header = cartTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50));


        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.getViewport().setPreferredSize(new Dimension(700, 300));
        scrollPane.setBorder(new EmptyBorder(20,0,0,0));

        top.add(scrollPane);
        cartFrame.add(top, BorderLayout.NORTH);
        return shoppingCartTable;

    }

    private void showReceipt() {
        bottom.removeAll();
        //Use a Grid Layout to set data in a table-like format
        JPanel priceDetailsPanel = new JPanel(new GridBagLayout());

        //Put data in Grid format
        receiptDetails(priceDetailsPanel, "Total ", calculateTotalPrice() +" "+( pound.getSymbol() ), GridBagConstraints.LINE_START);
        receiptDetails(priceDetailsPanel, "Three Items in same Category Discount ", String.valueOf(discountsCalculate())+" "+(pound.getSymbol()), GridBagConstraints.LINE_START);
        boolean firstTime = Console.isFirstTime();
        if (firstTime){
            receiptDetails(priceDetailsPanel, "First Purchase Discount ", firstTimeDiscountCal() +" "+(pound.getSymbol()), GridBagConstraints.LINE_START);
        }
        receiptDetails(priceDetailsPanel, "Final Total", calculateFinalPrice() +" "+(pound.getSymbol()), GridBagConstraints.LINE_START);

        priceDetailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 450, 80, 10));

        cartFrame.add(priceDetailsPanel, BorderLayout.SOUTH);
        bottom.revalidate();
        bottom.repaint();
        cartFrame.revalidate();
        cartFrame.repaint();
    }

    private void receiptDetails(JPanel panel, String label, String value, int alignment) {
        // Create GridBagConstraints for the label
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = panel.getComponentCount() / 2; // To alternate between label and value
        gbcLabel.anchor = GridBagConstraints.LINE_END;
        gbcLabel.insets = new Insets(5, 5, 5, 60);

        GridBagConstraints gbcValue = new GridBagConstraints();// Create GridBagConstraints for the value
        gbcValue.gridx = 1;
        gbcValue.gridy = panel.getComponentCount() / 2;
        gbcValue.anchor = alignment; // Set the alignment for the value column
        gbcValue.insets = new Insets(5, 5, 5, 200);

        panel.add(new JLabel(label), gbcLabel);// Add label to the panel using the GridBagConstraints for label
        panel.add(new JLabel(value), gbcValue);// Add value to the panel using the GridBagConstraints for value
    }

    private double calculateTotalPrice() {//Calculate total price
        // Create a DefaultTableModel for the shopping cart
        DefaultTableModel shoppingCart = createShoppingCartTable();
        double sum = 0;
        // Iterate through each row in the shopping cart
        for (int row = 0; row < shoppingCart.getRowCount(); row++) {
            // Get the value from the specified column (column index 2) in the current row
            Object value = shoppingCart.getValueAt(row, 2);

            // Check if the value is not null and is an instance of Number
            if (value != null && value instanceof Number) {
                // Add the double value of the Number to the sum
                sum += ((Number) value).doubleValue();
            }
        }
        return sum;
    }

    private double calculateFinalPrice(){// Price after discounts
        boolean firstTime = Console.isFirstTime();
        double firstDiscount;
        if (firstTime){//Checking if eligible for first time discount
            firstDiscount = firstTimeDiscountCal();
        }
        else {
            firstDiscount = 0.0;
        }
        //Calculate discount
        double finalPrice = calculateTotalPrice() -discountsCalculate() - firstDiscount;
        return finalPrice;
    }

    private double discountsCalculate() {
        HashMap<String, Integer> categoryCount = new HashMap<>();

        for (Product product : cart.getCartItems()) {
            String category = product instanceof Electronics? "Electronics": "Clothing";
            if(cart.getQuantity(product.getProductID())>1){
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + cart.getQuantity(product.getProductID()));
            }
            else {
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            }
        }
        double discount = 0.0;

        for (int count : categoryCount.values()) {
            if (count >= 3) {
                discount += 0.2;
            }
        }

        double totalDiscount = calculateTotalPrice() * discount;

        return totalDiscount;
    }

    private double firstTimeDiscountCal(){
        double firstDiscount = calculateTotalPrice() * 0.1;
        return firstDiscount;
    }



}

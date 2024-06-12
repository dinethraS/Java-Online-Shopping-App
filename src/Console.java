import java.util.*;

public class Console {
    private static Validate v1 = new Validate(); //Instance of Validate class to access the validation methods
    private static Map<String,String> credentials = User.loadFile();//hashMap containing username and password and will load the existing data from the file
    private static ArrayList<User> users = new ArrayList<>();// To store user objects
    private static boolean FirstTime = false; // Getting value to use to calculate discount


    static ShoppingManager manager = new WestminsterShoppingManager();

    public static boolean isFirstTime() {
        return FirstTime;
    }

    public static void main(String[] args) {
        manager.load();
        boolean value = true;
        while (value){
//          Loading already added products
            System.out.println("""
                
                WELCOME TO WESTMINSTER SHOPPING CENTRE
                    
                IF CUSTOMER PRESS (c)
                IF MANAGER PRESS (m)
                Press 0 to EXIT
                :""");
            Scanner sc = new Scanner(System.in);
            //Using pre-validating method from Validate class to get input
            String option = sc.next();
            switch (option.toLowerCase()) {
                case "m" -> {
                    managerFunction();
                    value = false;
                }
                case "c" -> {
                    userFunctions();
                    value = false;
                }
                case "0" -> System.exit(0);
                default -> System.out.println("Enter an Valid Option!! ");
            }
        }






    }

    public static void managerFunction(){
        while (true) {
            //To display the managers options
            manager.displayConsoleMenu();
            Scanner sc = new Scanner(System.in);
            String option = v1.validateInput(sc,"Enter Option : ",String.class);
            switch (option) {
                case "0" -> System.exit(0);
                case "1" -> manager.addProduct();
                case "2" -> manager.deleteProduct();
                case "3" -> manager.printProducts();
                case "4" -> manager.saveProducts();
                default -> System.out.println("Invalid option!");
            }
        }
    }

    public static void userFunctions(){
        boolean value = true;
        while (value){
            Scanner sc = new Scanner(System.in);
            String option = v1.validateInput(sc,"""
           Are you a first time user?
           Press 'y' for YES
           Press 'n' for NO
           Press 0 to EXIT
           :
            """, String.class);
            switch (option.toLowerCase()) {
                case "y" -> {
                FirstTime = true;
                String uname,pass,email;
                    while (true){
                        uname = v1.validateInput(sc, "Enter a username: ", String.class);
                        //Using hashMap with credentials and checking if username is already there.
                        if (credentials.containsKey(uname)) {
                            System.out.println("This username already exists");
                        }
                        else {
                            break;
                        }
                    }
                    while (true){
                        pass = v1.validateInput(sc, "Enter a password: ", String.class);
                        //check if password has a minimum of 6 characters
                        if (pass.length() < 6) {
                            System.out.println("Password must contain 6 characters minimum!!");
                        }
                        else {
                            break;
                        }
                    }
                    while (true){
                        email = v1.validateInput(sc, "Enter a valid email: ", String.class);
                        //check if email has @ sign
                        if (!email.contains("@")) {
                            System.out.println("Invalid Email!!");
                        }
                        else {
                            break;
                        }
                    }

                    //New User details are added to a new arrayList
                    User newUser = new User(uname, pass, email);
                    users.add(newUser);
                    credentials.put(uname, pass);
                    User.saveFile(credentials);
                    //GUI frame will be visible
                    MainFrame gui = new MainFrame();
                    value = false;
                }


                case "n" -> {
                    while (true) {
                        String uname = v1.validateInput(sc, "Enter Username: ", String.class);
                        String pass = v1.validateInput(sc, "Enter Password: ", String.class);
                        //check if key and value match in hashMap
                        if (!(credentials.containsKey(uname) && ((credentials.get(uname)).equals(pass)))) {
                            System.out.println("Incorrect Username or Password");
                        } else {
                            //GUI frame will be visible
                            new MainFrame();
                            value = false;
                            break;
                        }
                    }
                }

                case "0" -> System.exit(0);
                default -> System.out.println("Invalid option");
            }

        }

    }



}


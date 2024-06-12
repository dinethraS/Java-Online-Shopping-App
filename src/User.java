import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable{
    private final String uname;
    private final String password;
    private final String email;

    static void saveFile(Map<String, String> hashMap) {// Save a Map<String, String> to a file using serialization
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("hash.txt"))) {
            // Write the HashMap to the file using ObjectOutputStream
            oos.writeObject(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Map<String, String> loadFile() {// Load a Map<String, String> from a file using deserialization
        Map<String, String> hashMap = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("hash.txt"))) {
            // Read the HashMap from the file using ObjectInputStream
            hashMap = (Map<String, String>) ois.readObject();
        } catch (FileNotFoundException ignored) {//Ignored if file not found
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hashMap;//Return loaded hashMap
    }


    public User(String uname, String password, String email) {
        this.uname = uname;
        this.password = password;
        this.email = email;
    }
}

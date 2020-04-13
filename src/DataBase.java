import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase
{
    private List<User> contacts = new ArrayList<>();

    DataBase() throws IOException {
        readData();
    }

    private void readData() throws FileNotFoundException, IOException {
        File file = new File("data.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            String name = line;
            line = reader.readLine();
            String address = line;
            contacts.add(new User(name, address));
        }
        reader.close();
        //и добавим самого себя
        contacts.add(new User("Сохраненки","127.0.0.1"));
    }

    public List<User> getContacts() {
        return contacts;
    }

    /*private void writeData()
    {
        String name;
        String address;
        FileWriter out = new FileWriter("data.txt");
        for(User user : contacts)
        {
        name = user.getName();
        address = user.getAddress();
        out.write(name + System.getProperty("line.separator") + address + System.getProperty("line.separator"));
        }
        out.close();
    }*/
}

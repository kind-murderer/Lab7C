import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase
{
    private List<User> contacts = new ArrayList<>();
    private List<User> logins = new ArrayList<>();

    DataBase() throws IOException {
        readContacts();
        readLogins();
    }

    private void readContacts() throws FileNotFoundException, IOException {
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

    private void readLogins() throws FileNotFoundException, IOException {
        File file = new File("logins.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            String name = line;
            line = reader.readLine();
            String password = line;
            logins.add(new User(name, password));
        }
        reader.close();
    }

    public List<User> getContacts() {
        return contacts;
    }

    public List<User> getLogins() {
        return logins;
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

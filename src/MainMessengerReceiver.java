import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainMessengerReceiver
{
    //contacts
    DataBase dataBase;

    //listeners
    private List<MessageListener> dialoglisteners = new ArrayList<>();

    private final int SERVER_PORT = 4567;

    MainMessengerReceiver(MainFrame parent) //КОНСТРУКТОР
    {
        //MainFrame тут только ради роли родителя-окна при выносе ошибок
        try {
            dataBase = new DataBase();
        } catch(FileNotFoundException e1)
        {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Файл не найден, невозможно прочесть контакты", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            parent.dispose();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Ошибка в чтении файла", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            parent.dispose();
        }
        startServer(parent);

    }

    private void startServer(MainFrame parent)
    {
        //Создание и запуск потока-отработчика запросов
        new Thread(new Runnable(){
            public void run ()
            {
                try{
                    final ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

                    while(true)
                    {
                        /* Искомый ждёт пока кто-либо не захочет подсоединится к нему,
                         и когда это происходит возвращает объект типа Socket, то есть воссозданный клиентский сокет.*/
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        //Читаем имя отправителя
                        final String senderName = in.readUTF();
                        //Читаем сообщение
                        final String message = in.readUTF();
                        //Закрываем соединение
                        socket.close();
                        //Выделяем IP-адрес
                        final String address = ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().getHostAddress();

                        //Уведомили слушателей
                        notifyListeners(new User(senderName, address), message);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parent, "Ошибка в работе сервера", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    parent.dispose();
                }
            }
        }).start();
    }

    public void addUser(JFrame parent, User newbie) {
        //проверим, не существует ли с таким именем уже (адреса одинаковые, но имена разные -ок)
        String name = newbie.getName();
        String address = newbie.getAddress();
        boolean res = false;
        if(name != null && address != null)
        {
            for(User contact : dataBase.getContacts()) {
                if (contact.getName().equals(name)) {
                    res = true;
                    break;
                }
            }
            if(!res) //если такого нет
            {
                dataBase.getContacts().add(newbie);
                try {
                    FileWriter out = new FileWriter("data.txt", true);
                    out.write(name + System.getProperty("line.separator") + address + System.getProperty("line.separator"));
                    out.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parent, "Файл не найден, невозможно добавить контакт", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    parent.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Такой контакт уже существует", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public int getServerPort() {
        return SERVER_PORT;
    }
    //Методы для наблюдателей
    public void addMessageListener(MessageListener listener) {
        synchronized (dialoglisteners) {
            dialoglisteners.add(listener);
        } }
    private void notifyListeners(User sender, String message) {
        synchronized (dialoglisteners) {
            for (MessageListener listener : dialoglisteners) {
                listener.messageReceived(sender, message);
            } } }
            /*public void removeMessageListener(MessageListener listener) {
        synchronized (dialoglisteners) {
            dialoglisteners.remove(listener);
        } }*/
}

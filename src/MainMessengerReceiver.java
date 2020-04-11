import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainMessengerReceiver
{

    private User sender;

    //listeners
    private List<MessageListener> dialoglisteners = new ArrayList<>();

    private final int SERVER_PORT = 4567;

    //СДЕЛАТЬ ЧТО-НИБУДЬ С СЕНДЕРОМ Х2 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    MainMessengerReceiver(MainFrame parent) //КОНСТРУКТОР
    {
        //MainFrame тут только ради роли родителя-окна при выносе ошибок
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
                        notifyListeners(senderName, address, message);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace(); //
                    JOptionPane.showMessageDialog(parent, "Ошибка в работе сервера", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    public int getServerPort() {
        return SERVER_PORT;
    }
    //Методы для наблюдателей
    public void addMessageListener(MessageListener listener) {
        synchronized (dialoglisteners) {
            dialoglisteners.add(listener);
        } }
    public void removeMessageListener(MessageListener listener) {
        synchronized (dialoglisteners) {
            dialoglisteners.remove(listener);
        } }
    //КОГДА СДЕЛАЮ КЛАСС Peer ВОЗМОЖНО String sender+address можно будет убрать, тк все будет в Peer Sender!!!!!!!!!!!!!!!!!!LATER!!!!!!!!!!!
    private void notifyListeners(String sender, String address, String message) {
        synchronized (dialoglisteners) {
            for (MessageListener listener : dialoglisteners) {
                listener.messageReceived(sender, address, message);
            } } }
}

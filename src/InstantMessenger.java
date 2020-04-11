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

public class InstantMessenger
{

    private User sender;

    //listeners
    private List<MessageListener> listeners_dialogFrames = new ArrayList<>();

    private static final int SERVER_PORT = 4567;

    //СДЕЛАТЬ ЧТО-НИБУДЬ С СЕНДЕРОМ Х2 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    InstantMessenger(MainFrame parent) //КОНСТРУКТОР
    {
        //DialogFrame тут только ради роли родителя-окна при выносе ошибок
        //не хочу читать с него напрямую с полей, лучше буду передавать String в методы, так, наверное, получится универсальнее
        //а...и еще чтобы добавиться в список слушателей



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

                    while(!Thread.interrupted())
                    {
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

    public void sendMessage(String senderName, String destinationAddress,
                            String message) throws UnknownHostException, IOException
    {
            //Создаем сокет для соединения
            final Socket socket = new Socket(destinationAddress, SERVER_PORT);

            //Открываем поток вывода данных
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //Записываем в поток имя
            out.writeUTF(senderName);
            //Записываем в поток сообщение
            out.writeUTF(message);
            //Закрываем сокет
            socket.close();

    }

    //Методы для наблюдателей
    public void addMessageListener(MessageListener listener) {
        synchronized (listeners_dialogFrames) {
            listeners_dialogFrames.add(listener);
        } }
    public void removeMessageListener(MessageListener listener) {
        synchronized (listeners_dialogFrames) {
            listeners_dialogFrames.remove(listener);
        } }
    //КОГДА СДЕЛАЮ КЛАСС Peer ВОЗМОЖНО String sender+address можно будет убрать, тк все будет в Peer Sender!!!!!!!!!!!!!!!!!!LATER!!!!!!!!!!!
    private void notifyListeners(String sender, String address, String message) {
        synchronized (listeners_dialogFrames) {
            for (MessageListener listener : listeners_dialogFrames) {
                listener.messageReceived(sender, address, message);
            } } }
}

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class DialogCommunication  implements MessageListener
{
    private DialogPanel mydialog;

    //User contact; возьмем с диалога через get
    private int SERVER_PORT;

    DialogCommunication(DialogPanel mydialog, MainMessengerReceiver myMessenger)
    {
        this.mydialog = mydialog;
        DialogCommunication.this.SERVER_PORT = myMessenger.getServerPort();
    }

    public void sendMessage(String senderName, User contact, String message) throws UnknownHostException, IOException
    {
        //Создаем сокет для соединения
        final Socket socket = new Socket(contact.getAddress(), SERVER_PORT);

        //Открываем поток вывода данных
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        //Записываем в поток имя
        out.writeUTF(senderName);
        //Записываем в поток сообщение
        out.writeUTF(message);
        //Закрываем сокет
        socket.close();

    }

    public void messageReceived(User sender, String message)
    {
        //блин ну нужен еще поток, чтоб вежливо попросить записать текст в textFieldIncoming,
        // ведь туда пишется из DialogPanel иногда, потом надо спрость норм ли так делать
        //"секундный" поток(чуть отработал и сразу умер), созданный в функции, так вообще легально делать?
        //почему не использовала просто wait и notify с 1: блин, ну можно, если сделать поток тут статик, мб мб мб не знаю пройдет ли
        //почему не создать 1 экземпляр и хранить в полях DialogComm а потом wait notify: проблемы с передачей информации потоку
        //его run с фуннкционалом messageRec в конструкторе был бы, пришлось бы выделить поле под последнюю поступившую инфу и писать туда отсюда
        //но этот способ не пройдет, если слишком быстро приходят сообщения одно за другим

        //проверим, что нам
        if(sender.getName().equals(mydialog.getContact().getName()) && sender.getAddress().equals(mydialog.getContact().getAddress()))
        {
            new Thread(new Runnable() {
                public void run()
                {
                    // Выводим сообщение в текстовую область
                    //синхронозируем, мб, в тот же момент мы что-то сами отсылаем и там идет вывод
                    synchronized (mydialog.getTextAreaIncoming()) {
                        mydialog.getTextAreaIncoming().append(sender.getName() + " (" + sender.getAddress() + "): " + message + "\n");
                    }
                }
            }).start();
        }

    }
}

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;

    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;

    private static final String FRAME_TITLE = "Клиент мгновернных сообщений";

    //сервер порт перенесен в instantMessenger
    //экземпляр мессенджера
    private MainMessengerReceiver myMessenger;

    MainFrame()
    {
        super(FRAME_TITLE);
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth())/2 , (kit.getScreenSize().height - getHeight())/2);

        //СОЗДАНИЕ МЕССЕНДЖЕРА
        myMessenger = new MainMessengerReceiver(this);


        //панель с вкладками
        final JTabbedPane tabbedPane = new JTabbedPane();

        DialogPanel dialog1 = new DialogPanel();
        DialogCommunication dialog1Communication = new DialogCommunication(dialog1, myMessenger);
        dialog1.setMyDialogCommunication(dialog1Communication);
        myMessenger.addMessageListener(dialog1Communication);

        tabbedPane.addTab("Вкладка диалога", dialog1);

        getContentPane().add(tabbedPane);


    }

    public static void main(String[] args) {
        /*В главном потоке JVM запускает метод main() приложения, а также все
        методы, которые вызываются из него, при этом главному потоку
        автоматически присваивается имя main.*/

        //SwingUtilities.invokeLater(new Runnable() {
            //public void run() {
                //final DialogFrame frame = new DialogFrame();
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //frame.setVisible(true);

                //блин, я, походу не оставлю здесь разделение действий потоками , надо будет спросить потом все равно
                //сделаю поток в конструкторе MainMessenger
                final MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
           //}
       // });
    }
}

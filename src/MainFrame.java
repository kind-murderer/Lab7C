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
    InstantMessenger myMessenger;

    MainFrame()
    {
        super(FRAME_TITLE);
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth())/2 , (kit.getScreenSize().height - getHeight())/2);

        //СОЗДАНИЕ МЕССЕНДЖЕРА
        myMessenger = new InstantMessenger(this);


        //панель с вкладками
        final JTabbedPane tabbedPane = new JTabbedPane();

        DialogPanel dialog1 = new DialogPanel(myMessenger);
        tabbedPane.addTab("Вкладка диалога", dialog1);

        getContentPane().add(tabbedPane);


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //final DialogFrame frame = new DialogFrame();
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //frame.setVisible(true);
                //ПОТОМ РАЗОБРАТЬСЯ КАК БУДЕМ ДЕЛАТЬ ОКОШКО ДЛЯ ДИАЛОГА
                //блин, я, наверн оставлю здесь пока разделение действий потоками , надо будет спросить потом
                final MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

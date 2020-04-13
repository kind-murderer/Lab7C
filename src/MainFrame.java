import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainFrame extends JFrame
{
    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;

    private static final String FRAME_TITLE = "Клиент мгновернных сообщений";

    //панель с вкладками;
     private static final JTabbedPane tabbedPane =  new JTabbedPane();

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

        //панель с кнопками
        JPanel buttonPanel = new JPanel();
        JButton buttonAdd = new JButton("Начать новую беседу");
        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Выбрать из контактов?",
                        "Начать новую беседу",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if(result == JOptionPane.YES_OPTION){
                    int size = myMessenger.getDataBase().getContacts().size();
                    String[] cont = new String[size];
                    for(int i = 0; i < size; i++)
                    {
                        cont[i] = myMessenger.getDataBase().getContacts().get(i).getName();
                    }
                    String choiceName = (String)JOptionPane.showInputDialog(MainFrame.this,
                            "Выберите собеседника :",
                            "Контакты", JOptionPane.QUESTION_MESSAGE, null,
                             cont, cont[0]);
                    User choice = null;
                    for(User element : myMessenger.getDataBase().getContacts()) { //ищем по имени кого выбрали
                       if(choiceName.equals(element.getName()))
                       {
                           choice = element;
                       }
                    }
                    if(choice == null)
                    {
                        System.out.println("Smth went wrong");
                    } else {
                        newChat(MainFrame.tabbedPane, choice);
                    }
                }

                if(result == JOptionPane.NO_OPTION){
                    MiniFrame miniFrame = new MiniFrame("Имя пользователя: ", "Адрес: ", "Новый контакт", 1);
                    //экземпляр внутреннего класса miniFrame
                    }
            }
        });
        buttonPanel.add(buttonAdd);

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(tabbedPane);

    }

    private void newChat(JTabbedPane tabbedPane, User contact)
    {
        DialogPanel dialog1 = new DialogPanel(contact);
        DialogCommunication dialog1Communication = new DialogCommunication(dialog1, myMessenger);
        dialog1.setMyDialogCommunication(dialog1Communication);
        myMessenger.addMessageListener(dialog1Communication);
        tabbedPane.addTab(contact.getName(), dialog1);
    }

    //внутренний класс miniFrame
    private class MiniFrame extends JFrame
    {
        private String[] text = new String[2];
        private MiniFrame(String label1text, String label2text, String tittle, int purpose) //конструктор
        {
            super(tittle);
            Box hboxForLabel1 = Box.createHorizontalBox();
            JLabel label1 = new JLabel(label1text);
            hboxForLabel1.add(label1);

            Box hboxForField1 = Box.createHorizontalBox();
            JTextField textField1 = new JTextField();
            int prefHeight = textField1.getPreferredSize().height;
            textField1.setMaximumSize(new Dimension(Integer.MAX_VALUE, prefHeight));
            hboxForField1.add(textField1);

            Box hboxForLabel2 = Box.createHorizontalBox();
            JLabel label2 = new JLabel(label2text);
            hboxForLabel2.add(label2);

            Box hboxForField2 = Box.createHorizontalBox();
            JTextField textField2 = new JTextField();
            prefHeight = textField2.getPreferredSize().height;
            textField2.setMaximumSize(new Dimension(Integer.MAX_VALUE, prefHeight));
            hboxForField2.add(textField2);

            Box hboxForButton = Box.createHorizontalBox();
            JButton buttonOK = new JButton("OK");
            buttonOK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //purpose == 1 for new contact
                    //purpose == 2 for registration
                    //purpose == 3 for log in
                    text[0] = textField1.getText();
                    text[1] = textField2.getText();
                    if(purpose == 1)
                    {
                        User newbie = new User(text[0], text[1]);
                        myMessenger.addUser(MainFrame.this, newbie);
                        newChat(MainFrame.tabbedPane, newbie);
                    }
                    MiniFrame.this.dispose();
                }
            });
            hboxForButton.add(buttonOK);

            Box contentBox = Box.createVerticalBox();
            contentBox.add(Box.createVerticalGlue());
            contentBox.add(hboxForLabel1);
            contentBox.add(Box.createVerticalStrut(10));
            contentBox.add(hboxForField1);
            contentBox.add(Box.createVerticalStrut(20));
            contentBox.add(Box.createVerticalGlue());
            contentBox.add(hboxForLabel2);
            contentBox.add(Box.createVerticalStrut(10));
            contentBox.add(hboxForField2);
            contentBox.add(Box.createVerticalStrut(20));
            contentBox.add(Box.createVerticalGlue());
            contentBox.add(hboxForButton);
            contentBox.add(Box.createVerticalGlue());

            getContentPane().add(contentBox);

            setMinimumSize(new Dimension(300, 250));
            final Toolkit kit = Toolkit.getDefaultToolkit();
            setLocation((kit.getScreenSize().width - getWidth())/2 , (kit.getScreenSize().height - getHeight())/2);
            setVisible(true);
        }//конец конструктора
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

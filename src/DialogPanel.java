import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DialogPanel extends JPanel implements MessageListener
{
    private static final String FRAME_TITLE = "Диалог";

    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;

    private static final int FROM_FIELD_DEFAULT_COLUMNS = 10;
    private static final int TO_FIELD_DEFAULT_COLUMNS = 20;

    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTCOMING_AREA_DEFAULT_ROWS = 5;

    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;

    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;

    private final JTextArea textAreaIncoming;
    private final JTextArea textAreaOutgoing;

    //экземпляр мессенджера, передаст мэйнфрэйм, мессенджер один на все окошки
    InstantMessenger myMessenger;

    //КОНСТРУКТОР
    public DialogPanel(InstantMessenger myMessenger)
    {

       // setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        //final Toolkit kit = Toolkit.getDefaultToolkit();
       // setLocation((kit.getScreenSize().width - getWidth())/2 , (kit.getScreenSize().height - getHeight())/2);

        //текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);

        //контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);

        //Подписи полей
        final JLabel labelFrom = new JLabel ("От");
        final JLabel labelTo = new JLabel ("Получатель");

        //Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);

        //Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTCOMING_AREA_DEFAULT_ROWS, 0);

        //Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);


        //СДЕЛАТЬ ЧТО-НИБУДЬ С СЕНДЕРОМ Х1 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //СОЗДАНИЕ МЕССЕНДЖЕРА
        this.myMessenger = myMessenger;
        myMessenger.addMessageListener(this);

        //Панель ввода сообщения
        final JPanel messagePanel = new JPanel();
        messagePanel.setBorder(
                BorderFactory.createTitledBorder("Сообщение"));

        //Кнопка отправки сообщения
        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Получаем необходимые параметры
                final String senderName = textFieldFrom.getText();
                final String destinationAddress = textFieldTo.getText();
                final String message = textAreaOutgoing.getText();
                //Убеждаемся, что пистолет заряжен,     т.e. что поля не пустые
                if (senderName.isEmpty())
                {
                    JOptionPane.showMessageDialog(DialogPanel.this,
                            "Введите имя отправителя","Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (destinationAddress.isEmpty())
                {
                    JOptionPane.showMessageDialog(DialogPanel.this,
                            "Введите адрес узла-получателя","Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (message.isEmpty())
                {
                    JOptionPane.showMessageDialog(DialogPanel.this,
                            "Введите текст сообщения","Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //Передаем дело отправки мессенджеру
                try {
                    myMessenger.sendMessage(senderName, destinationAddress, message);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(DialogPanel.this, "Не удалось отправить сообщение: узел-адресат не найден", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(DialogPanel.this, "Не удалось отправить сообщение", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }

                //Пишем в текстовую область вывода, что отправили свое сообщение
                textAreaIncoming.append("Я -> " + destinationAddress + ": " + message + "\n");
                //Очищаем текстовую область ввода
                textAreaOutgoing.setText("");
            }
        });

        //Компоновка элементов панели "Сообщение"
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);

        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(layout2.createSequentialGroup()
                                        .addComponent(labelFrom)
                                        .addGap(SMALL_GAP)
                                        .addComponent(textFieldFrom)
                                        .addGap(LARGE_GAP)
                                        .addComponent(labelTo)
                                        .addGap(SMALL_GAP)
                                        .addComponent(textFieldTo))
                                .addComponent(scrollPaneOutgoing)
                                .addComponent(sendButton))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                        .addContainerGap()
            .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFrom)
                    .addComponent(textFieldFrom)
                    .addComponent(labelTo)
                    .addComponent(textFieldTo))
            .addGap(MEDIUM_GAP)
            .addComponent(scrollPaneOutgoing)
            .addGap(MEDIUM_GAP)
            .addComponent(sendButton)
            .addContainerGap());
         //Компановка элементов панели
        //ТУТ ПОТОМ ПОСМОТРЕТЬ НА ОШИБКИ, ЕСЛИ НЕ БУДЕТ РАБОТАТЬ
        final GroupLayout layout1 = new GroupLayout(this);
        setLayout(layout1);

        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                        .addContainerGap()
                        .addGroup( layout1.createParallelGroup()
                                .addComponent(scrollPaneIncoming)
                                .addComponent(messagePanel))
                        .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());

    } //КОНЕЦ КОНСТРУКТОРА

    public void messageReceived(String senderName, String address, String message)
    {
        // Выводим сообщение в текстовую область
        textAreaIncoming.append(senderName + " (" + address + "): " + message + "\n");
    }



}

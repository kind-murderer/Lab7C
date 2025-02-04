import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DialogPanel extends JPanel
{

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

    private User contact;

    //dialogCommunication берет на себя сетевое взаимодействие
    private DialogCommunication myDialogCommunication;

    //КОНСТРУКТОР
    public DialogPanel(User contact, User USER)
    {

        this.contact = contact;
        final String destinationAddress = contact.getAddress();

        //текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);

        //контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
        textAreaIncoming.setText("< Вы открыли диалог >\n");
        //Подписи полей
        final JLabel labelFrom = new JLabel ("От");
        final JLabel labelTo = new JLabel ("Получатель");

        //Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldFrom.setText(USER.getName());
        textFieldFrom.setEditable(false);
        textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
        textFieldTo.setText(contact.getAddress());
        textFieldTo.setEditable(false);
        //Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTCOMING_AREA_DEFAULT_ROWS, 0);

        //Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);

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
                //final String destinationAddress = textFieldTo.getText();
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
                    myDialogCommunication.sendMessage(senderName, contact, message);
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
                //синхронизирован, тк в это время может что-то приходить к нам и выводиться
                synchronized (textAreaIncoming) {
                    textAreaIncoming.append("Я -> " + destinationAddress + ": " + message + "\n");
                }
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

    public JTextArea getTextAreaIncoming() {
        return textAreaIncoming;
    }

    public User getContact() {
        return contact;
    }

    public void setMyDialogCommunication(DialogCommunication myDialogCommunication) {
        this.myDialogCommunication = myDialogCommunication;

    }
}

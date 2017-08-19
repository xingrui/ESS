package src.panels;

import javax.swing.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;
import java.awt.*;

import src.share.*;
import src.database.Database;

@SuppressWarnings("serial")
public class AdminPanel extends JPanel {

    private Queue queue[];

    private Database database;

    private Indicator[] indicator;

    private JLabel tag = new JLabel("Input elevatorID here");

    private JTextField input = new JTextField(6);

    private JButton start = new JButton("START");

    private JButton stop = new JButton("STOP");

    private JButton getRecords = new JButton("getRecords");

    private JButton logOut = new JButton("Log Out");

    private JButton auto = new JButton("Auto Play");

    private ActionListener a = new Listener(true), b = new Listener(false);

    private JTextArea area = new JTextArea(8, 30);

    private int elevatorNumber;

    JPanel panel1 = new JPanel();

    Login panelLogIn = new Login();

    /* initinalize the admin panel. */
    private void initinal()
    {
        panel1.setLayout(new GridLayout(3, 2, 4, 4));
        panel1.add(start);
        panel1.add(stop);
        panel1.add(tag);
        panel1.add(input);
        panel1.add(getRecords);
        panel1.add(logOut);
        panel1.add(auto);
        input.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    getRecords.doClick();
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        getRecords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                area.setText("");

                try {
                    int id = Integer.parseInt(input.getText());
                    Vector<String> v = database.getRecords(id);
                    Iterator<String> it = v.iterator();

                    while (it.hasNext()) {
                        area.append(it.next() + "\n");
                    }
                } catch (NumberFormatException e1) {
                    input.setText("WRONG FORMAT");
                }
            }
        });
        logOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choose = JOptionPane.showConfirmDialog(null,
                             "Are you sure to logout?");

                if (choose == 0) {
                    area.setText("");
                    stop.doClick();
                    panel1.setVisible(false);
                    panelLogIn.name.setText("");
                    panelLogIn.password.setText("");
                    panelLogIn.setVisible(true);
                }
            }
        });
        auto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                queue[0].enqueue(new AdminMessage(e.getActionCommand()));

                // change the text of the command.
                if (e.getActionCommand().equals("Auto Play"))
                    auto.setText("CancelAutoPlay");
                else
                    auto.setText("Auto Play");
            }
        });
        auto.setEnabled(false);
    }

    public AdminPanel(Queue[] queue, Database database)
    {
        initinal();
        add(panelLogIn);
        elevatorNumber = queue.length;
        this.queue = queue;
        this.database = database;
        indicator = new Indicator[elevatorNumber];

        for (int i = 0; i < elevatorNumber; i++) {
            indicator[i] = new Indicator();
            panel1.add(indicator[i]);
        }

        area.setEditable(false);
        start.addActionListener(a);
        add(new JScrollPane(area));
        add(panel1);
        panel1.setVisible(false);
    }

    private class Listener implements ActionListener {
        private boolean type;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            for (int i = 0; i < elevatorNumber; i++) {
                queue[i].enqueue(new AdminMessage(e.getActionCommand()));
                database.addControlRecords(i, e.getActionCommand());
            }

            if (type) {
                auto.setEnabled(true);
                start.removeActionListener(a);
                stop.addActionListener(b);
            } else {
                auto.setEnabled(false);
                auto.setText("Auto Play");
                stop.removeActionListener(b);
                start.addActionListener(a);
            }
        }

        public Listener(boolean i)
        {
            type = i;
        }
    }

    /** set the status of the elevator according to the elevatorID. */
    public void setStatus(int elevatorID, int currentFloor, String status)
    {
        database.updateStatus(elevatorID, status, currentFloor);
        indicator[elevatorID].updateStatus(currentFloor, status);
    }

    /** Process the emergency event. */
    public void emergency(int elevatorID)
    {
        database.addControlRecords(elevatorID, "emergency");
        int choose = JOptionPane.showConfirmDialog(null,
                     "Stop the elevator now?");

        if (choose == 0)
            stop.doClick();
    }

    /* The login panel of the admin panel. */
    private class Login extends JPanel implements ActionListener {
        private JLabel p1 = new JLabel("Name:");

        private JLabel p2 = new JLabel("Password:");

        private JTextField name = new JTextField(10);

        private JPasswordField password = new JPasswordField(10);

        private JButton login = new JButton("Login");

        private JPanel panel = new JPanel();
        {
            panel.setLayout(new GridLayout(3, 2, 5, 5));
            panel.add(p1);
            panel.add(name);
            panel.add(p2);
            panel.add(password);
            panel.add(login);
            login.addActionListener(this);
            password.setEchoChar('*');
            password.addKeyListener(new KeyListener() {

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                        login.doClick();
                }

                public void keyReleased(KeyEvent arg0) {
                }

                public void keyTyped(KeyEvent arg0) {
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String a = name.getText();
            String b = String.copyValueOf(password.getPassword());

            if (database.isAdmin(a, b)) {
                this.setVisible(false);
                panel1.setVisible(true);
            } else
                JOptionPane.showMessageDialog(null,
                                              "Wrong name or password.\nPlease enter again!");
        }

        public Login()
        {
            add(panel);
            setSize(200, 150);
            setLocation(400, 300);
        }
    }
}

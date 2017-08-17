package src.main;

import javax.swing.*;
import src.panels.*;
import src.share.*;
import src.control.*;
import src.database.*;

/** Our Main function. */
public class myMain {
    /**
     * Create InnerPanel, OuterPanel, AdminPanel, messagequeue, controller,
     * mesagecenter,and so on.
     */
    public myMain()
    {
        // Database database = DatabaseMysqlImp.getInstance();
        Database database = DatabaseNullImp.getInstance();
        final Queue[] queue = new Queue[1];
        InnerPanel[] in = new InnerPanel[1];
        OuterPanel[] outer = new OuterPanel[10];
        AdminPanel admin = new AdminPanel(queue, database);
        MessageCenter mc = new MessageCenter(in, outer, admin);
        queue[0] = new Queue();
        new Controller(queue[0], mc);
        in[0] = new InnerPanel(queue[0], 0, database);

        for (int i = 0; i < 10; i++) {
            outer[i] = new OuterPanel(i + 1, queue, database);
        }

        new Admin(admin);
        new Outer(outer);
        new Inner(in[0]);
    }

    /** Admin frame. */
    @SuppressWarnings("serial")
    private class Admin extends JFrame {
        private AdminPanel admin;

        Admin(AdminPanel ad)
        {
            setTitle("AdminPanel");
            admin = ad;
            getContentPane().add(admin);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            setSize(400, 350);
            setLocation(600, 0);
        }
    }

    /** OutPanel frame. */
    @SuppressWarnings("serial")
    private class Outer extends JFrame {
        private OuterPanel[] outer;

        Outer(OuterPanel[] out)
        {
            outer = out;
            setTitle("OuterPanel");
            JPanel panel = new JPanel();

            for (int i = 9; i >= 0; i--)
                panel.add(outer[i]);

            getContentPane().add(panel);
            setVisible(true);
            setSize(250, 650);
            setLocation(350, 0);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    /** InnerPanel frame. */
    @SuppressWarnings("serial")
    private class Inner extends JFrame {
        private InnerPanel inner;

        Inner(InnerPanel inn)
        {
            inner = inn;
            setTitle("InnerPanel");
            getContentPane().add(inner);
            setVisible(true);
            setSize(400, 300);
            setLocation(600, 350);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    /** Our main Function. */
    public static void main(String[] args)
    {
        new myMain();
    }

}

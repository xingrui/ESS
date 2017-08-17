package src.panels;

import javax.swing.*;

@SuppressWarnings("serial")
/** Used to display the current status of the elevator to the user. */
class Indicator extends JPanel {

    private JLabel label1 = new JLabel(), label2 = new JLabel();

    public Indicator()
    {
        add(label1);
        add(label2);
        updateStatus(1, "idle");
    }

    /** update the status of the indicator */
    public void updateStatus(int currentFloor, String status)
    {
        label1.setText(currentFloor + "");

        if (status.equalsIgnoreCase("up"))
            label2.setIcon(new ImageIcon(getClass().getResource("UP.GIF")));
        else if (status.equalsIgnoreCase("down"))
            label2.setIcon(new ImageIcon(getClass().getResource("DOWN.GIF")));
        else {
            label2.setIcon(new ImageIcon(getClass().getResource("STOP.GIF")));
        }
    }
}

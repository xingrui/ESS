package src.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.share.Queue;
import src.database.Database;

/** this class represents the outerpanel. */
@SuppressWarnings("serial")
public class OuterPanel extends JPanel {

    private Queue[] queue;

    private boolean upListened, downListened;

    private JButton up = new JButton("UP");

    private JButton down = new JButton("DOWN");

    private ActionListener a = new Up(), b = new Down();

    private int floorNumber;

    private Indicator[] indicator;

    /** Update the Indicator status of this outerpanel. */
    public void updateStatus(int elevatorID, int currentFloor, String status)
    {
        indicator[elevatorID].updateStatus(currentFloor, status);
    }

    /**
     * flag true represents the administrator is logged in and these buttons can
     * be used now.flag flase represents the administrator has logged out and
     * these buttons can not be used now.
     */
    public void setAble(boolean flag)  // used for MessageCenter
    {
        up.setEnabled(flag);
        down.setEnabled(flag);
    }

    /** Every outerpanel has a floor number. */
    public OuterPanel(int floorNumber, Queue[] queue)
    {
        this.floorNumber = floorNumber;
        this.queue = queue;

        if (floorNumber != 1) {
            add(down);
            addListener(false);
        }

        if (floorNumber != 10) {
            add(up);
            addListener(true);
        }

        setAble(false);
        indicator = new Indicator[queue.length];

        for (int i = 0; i < queue.length; i++) {
            indicator[i] = new Indicator();
            add(indicator[i]);
        }
    }

    /** The request of the floor has been satified and the button can used again. */
    public void addListener(boolean flag)
    {
        if (flag) {
            up.setBackground(Color.WHITE);

            if (!upListened) {
                up.addActionListener(a);
                upListened = true;
            }
        }

        else {
            down.setBackground(Color.WHITE);

            if (!downListened) {
                down.addActionListener(b);
                downListened = true;
            }
        }
    }

    /**
     * The elevator is in this floor. So there is no need to click this button.
     * or the previous request of the floor has not been satisfied.
     */
    public void removeListener(boolean flag)
    {
        if (flag) {
            up.removeActionListener(a);
            upListened = false;
        } else {
            down.removeActionListener(b);
            downListened = false;
        }
    }

    /* The listener that listens the up button. */
    private class Up implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            queue[0].enqueue(new OuterUpMessage(floorNumber));
            /*
             * to be realized::put the request to the proper queue;
             */
            Database.addFloorRequest(floorNumber, "UP");
            up.removeActionListener(a);
            up.setBackground(Color.RED);
            upListened = false;
        }
    }

    /* The listener that listens the down button. */
    private class Down implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            queue[0].enqueue(new OuterDownMessage(floorNumber));
            /*
             * to be realized::put the request to the proper queue;
             */
            Database.addFloorRequest(floorNumber, "DOWN");
            down.removeActionListener(b);
            down.setBackground(Color.RED);
            downListened = false;
        }
    }

    /** Used for AutoPlay. */
    public void click(boolean direction)
    {
        if (direction)
            up.doClick();
        else
            down.doClick();
    }
}

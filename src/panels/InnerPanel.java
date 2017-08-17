package src.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.share.Queue;
import src.database.Database;

@SuppressWarnings("serial")
/** Used to stand for the inner panel. */
public class InnerPanel extends JPanel {

	private int elevatorID;

	private Indicator indicator = new Indicator();

	private JButton button[] = new JButton[11];

	private boolean listened[] = new boolean[11];

	private ActionListener listener[] = new ActionListener[11];
	{
		for (int i = 1; i <= 10; i++) {
			button[i] = new JButton(i + "");
			listener[i] = new FloorListener(i);
			addListener(i);
		}
	}

	private Queue queue;

	private JButton emergency = new JButton("EMERGENCY");

	private JButton openDoor = new JButton("OPEN");

	private JButton closeDoor = new JButton("CLOSE");

	/* The listener of button 1 to 10. */
	private class FloorListener implements ActionListener {
		private int floor;

		public void actionPerformed(ActionEvent e) {
			queue.enqueue(new InnerMessage(floor));
			button[floor].removeActionListener(listener[floor]);
			button[floor].setBackground(Color.RED);
			Database.addControlRecords(elevatorID, e.getActionCommand());
		}

		public FloorListener(int i) {
			floor = i;
		}
	}

	/* The listener of the other buttons. */
	private class OtherListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			queue.enqueue(new OtherMessage(e.getActionCommand()));
			Database.addControlRecords(elevatorID, e.getActionCommand());
		}
	}

	/** Every InnerPanel has a request queue and an elevatorID. */
	public InnerPanel(Queue queue, int elevatorID) {
		this.elevatorID = elevatorID;
		this.queue = queue;
		setLayout(new GridLayout(5, 3, 5, 5));
		for (int i = 10; i > 0; i--) {
			add(button[i]);
		}
		removeListener(1);
		add(emergency);
		add(openDoor);
		add(closeDoor);
		add(indicator);
		emergency.addActionListener(new OtherListener());
		openDoor.addActionListener(new OtherListener());
		closeDoor.addActionListener(new OtherListener());
		setAble(false);
	}

	/** The request of the floor has been satified and the button can used again. */
	public void addListener(int i) {
		button[i].setBackground(Color.WHITE);
		if (!listened[i]) {
			button[i].addActionListener(listener[i]);
			listened[i] = true;
		}
	}

	/** The elevator is in this floor.So there is no need to click this button. */
	public void removeListener(int i) {
		button[i].removeActionListener(listener[i]);
		listened[i] = false;
	}

	/**
	 * flag true represents the administrator is logged in and these buttons can
	 * be used now.flag flase represents the administrator has logged out and
	 * these buttons can not be used now.
	 */
	public void setAble(boolean flag) {
		for (int i = 1; i <= 10; i++)
			button[i].setEnabled(flag);
		emergency.setEnabled(flag);
		openDoor.setEnabled(flag);
		closeDoor.setEnabled(flag);
	}

	/** Used to update the status of the elevator. */
	public void updateStatus(int currentFloor, String status) {
		indicator.updateStatus(currentFloor, status);
	}

	/** Used for the AutoPlay class. */
	public void click(int i) {
		button[i].doClick();
		System.out.println("Clicked");
	}
}

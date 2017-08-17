package src.elevator;

import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.net.URL;

import src.control.Controller;
import com.sun.j3d.utils.applet.MainFrame;

/**
 * This class is used to drive the elevator system by requiring the controller
 * whether to stop ,start and so on.
 */
@SuppressWarnings("serial")
public class Elevator extends JFrame {
	private enum Direct {
		UP, DOWN, NONE
	}

	private Controller controller;

	private Elevator3D t = new Elevator3D();

	private Thread topen, tstart, tstop, tmove;

	private TClose tclose;

	private final int SLEEPTIME = 10;

	private Tkeep tkeep;

	private double location = 1;

	private Direct direct = Direct.NONE;

	private boolean moving, opening;

	private Board board = new Board();

	public Elevator(Controller controller, boolean flag) {
		this.controller = controller;
		setTitle("Elevator");
		Container cp = getContentPane();
		cp.add(new JScrollPane(board));
		setSize(150, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (flag)
			new MainFrame(t, 345, 615);
		else
			setVisible(true);
		new TWait().start();
	}

	/** Get the current floor of the elevator. */
	public int getCurrentFloor() {
		return (int) location;
	}

	/** Open the door for some seconds. */
	public void open(int time) {
		// is there anther opening thread?
		if (topen != null && topen.isAlive())
			return;
		opening = true;
		// is the elevator is moving.
		if (moving) {
			opening = false;
			return;
		}
		/* The method above is used to prevent the deadLock and Conflict. */

		// stop the close thread if it exists.
		if (tclose != null && tclose.isAlive()) {
			tclose.flag = false;
			tclose.interrupt();
		}
		// stop the keep thread is it exists.
		if (tkeep != null && tkeep.isAlive()) {
			tkeep.flag = false;
			tkeep.interrupt();
		}
		// open the door
		topen = new TOpen();
		topen.start();
		// keeping door open for some seconds.
		tkeep = new Tkeep(time);
		tkeep.start();
	}

	/** Close the door. */
	public void close() {
		// is the elevator moving?
		if (moving)
			return;
		// ia the opening door thread alive?
		if (topen != null && topen.isAlive())
			return;
		// is anther closing door thread alive?
		if (tclose != null && tclose.isAlive())
			return;
		// stop the keep thread if exists.
		if (tkeep != null && tkeep.isAlive()) {
			tkeep.flag = false;
			tkeep.interrupt();
		}
		tclose = new TClose();
		tclose.start();
	}

	private void starting(boolean direction) {
		// is the tstart is already alive.
		if (tstart != null && tstart.isAlive())
			return;
		moving = true;
		// is the door opened.
		if (opening) {
			moving = false;
			return;
		}
		tstart = new TStart(direction);
		tstart.start();
	}

	private void moving(boolean direction) {
		// is anthor move thread alivc.
		if (tmove != null && tmove.isAlive())
			return;
		tmove = new TMove(direction);
		tmove.start();
	}

	private void stopping(int a) {
		// is anthor stop thread alive.
		if (tstop != null && tstop.isAlive())
			return;
		tstop = new TStop(a);
		tstop.start();
	}

	/* Thread for elevator staring */
	private class TStart extends Thread {
		private boolean direction;

		public TStart(boolean d) {
			direction = d;
		}

		public void run() {
			direct = direction ? Direct.UP : Direct.DOWN;
			for (int i = 0; i < 100; i++) {
				if (direction)
					location += i / 10 * 0.001;
				else
					location -= i / 10 * 0.001;
				t.setlocation(location);
				repaint();
				try {
					sleep(SLEEPTIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			moving(direction);
		}

	}

	/* Thread for elevator moving. */
	private class TMove extends Thread {
		private boolean direction;

		public TMove(boolean d) {
			direction = d;
		}

		public void run() {
			double distance;
			boolean flag = true;
			int[] a = new int[1];
			while (flag) {
				distance = location - (int) location;
				if (direction) {
					location += 0.01;
					// if the elevator is in the point that need to decide where
					// to stop or not and then ask the controller the question.
					if (Math.abs(distance - 0.45) < 0.001)
						if (controller.need_open_door(a)) {
							flag = false;
							stopping(a[0]);
						} else if ((int) location == 9) {
							flag = false;
							System.err.println("An error has happened!");
							stopping(0);
						}
				} else {
					location -= 0.01;
					// if the elevator is in the point that need to decide where
					// to stop or not and then ask the controller the question.
					if (Math.abs(distance - 0.55) < 0.001)
						if (controller.need_open_door(a)) {
							flag = false;
							stopping(a[0]);
						} else if ((int) location == 1) {
							flag = false;
							System.err.println("An error has happened!");
							stopping(0);
						}
				}
				t.setlocation(location);
				repaint();
				try {
					sleep(SLEEPTIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* Thread for elevator stopping. */
	private class TStop extends Thread {
		private int time;

		TStop(int t) {
			time = t;
		}

		public void run() {
			for (int i = 0; i < 100; i++) {
				if (direct == Direct.UP)
					location += (10 - i / 10) * 0.001;
				else if (direct == Direct.DOWN)
					location -= (10 - i / 10) * 0.001;
				t.setlocation(location);
				repaint();
				try {
					sleep(SLEEPTIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			URL music = getClass().getResource("notify.wav");
			AudioClip b = Applet.newAudioClip(music);
			b.play();
			location = Math.round(location);
			direct = Direct.NONE;
			opening = true;
			moving = false;
			if (time != 0) {
				open(time);
			} else
				opening = false;
		}
	}

	/*
	 * Thread for testing whether there is new command coming. When the elevator
	 * is idle ,Every certain time it ask the controller whether to start or
	 * open door.
	 */
	private class TWait extends Thread {
		public void run() {
			while (true) {
				try {
					sleep(50);
					if (!moving && !opening) {
						int num = controller.need_start();
						switch (num) {
						case 1:
							starting(true);
							break;
						case 2:
							starting(false);
							break;
						case 0:
							open(3);
							break;
						default:
							;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* Thread for opening the door. */
	private class TOpen extends Thread {
		public void run() {
			while (board.x < 100) {
				board.x++;
				board.repaint();
				t.open(board.x / 100.0);
				try {
					sleep(SLEEPTIME);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/* Thread for elevator is waiting for person to come in and out. */
	private class Tkeep extends Thread {
		int time;

		boolean flag = true;

		public Tkeep(int time) {
			this.time = time;
		}

		public void run() {
			try {
				topen.join();
				sleep(time * 1000);
				if (flag)
					close();
			} catch (InterruptedException e) {
			}
		}
	}

	/* Thread for the elevator is closing. */
	private class TClose extends Thread {
		boolean flag;

		public void run() {
			flag = true;
			while (board.x > 0 && flag) {
				board.x--;
				t.open(board.x / 100.0);
				board.repaint();
				try {
					sleep(SLEEPTIME);
				} catch (InterruptedException e) {
				}
			}
			if (flag) {
				opening = false;
			}
		}
	}

	/* The 2D graphic interface of the elevator. */
	private class Board extends JPanel {
		private int x = 0, num, y = 0;

		static final int X1 = 20, Y1 = 10, X = 4, Y = 100;

		static final int X2 = 120, FLOORHEIGHT = 59;

		static final int MID = (X1 + X2) / 2;

		static final int DIFF = (X2 - X1) / 2;

		public Board() {
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.drawLine(X1 - X - 1, Y1, X1 - X - 1, Y1 + 10 * FLOORHEIGHT);
			g.drawLine(X2 + X, Y1, X2 + X, Y1 + 10 * FLOORHEIGHT);
			for (int i = 0; i < 11; i++)
				g.drawLine(X1 - X - 1, Y1 + i * FLOORHEIGHT, X2 + X, Y1 + i
						* FLOORHEIGHT);
			g.setColor(Color.GREEN);
			y = (int) (FLOORHEIGHT * (10 - location));
			num = DIFF * x / 100;
			g.fillRect(X1 - X, Y1 + y + 1, DIFF - num + X, FLOORHEIGHT - 1);
			g.fillRect(MID + num, Y1 + y + 1, DIFF - num + X, FLOORHEIGHT - 1);
		}
	}
}

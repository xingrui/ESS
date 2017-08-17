package src.control;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.*;
import src.share.*;
import src.elevator.Elevator;
/**
 * the M of MVC modeling, determine the movement of the elevator 
 */

public class Controller {
	private Elevator elevator;

	private static int number = 0;

	private int elevatorID;

	// four types of state
	private static final int STOP = -1;

	private static final int IDLE = 0;

	private static final int UP = 1;

	private static final int DOWN = 2;

	// the seconds to wait before closing door
	private static final int THREE = 3;

	private static final int SIX = 6;

	// current state and floor
	private int currentFloor;

	private int state;

	// temp contains the message from message certain
	private Queue messages;

	// three arrays contain the cmds and requires
	private boolean[] innerRequest = new boolean[11];

	private boolean[] outerUp = new boolean[11];

	private boolean[] outerDown = new boolean[11];

	// where cmd and require from
	private MessageCenter mc;

	Controller(Queue from) {/* used for test */
		messages = from;
		mc = new MessageCenter(null, null, null);
		state = IDLE;
		currentFloor = 1;
		new CMD_REQ_Analyzer();
	}

	void setLocation(int location) /* used for test */{
		currentFloor = location;
	}

	void setStatus(boolean flag) {/* used for test */
		state = flag ? UP : DOWN;
	}

	public Controller(Queue from, MessageCenter mc, boolean flag) {
		elevator = new Elevator(this, flag);
		elevator.setLocation(200, 0);
		elevatorID = number++;
		currentFloor = 1;
		state = STOP;
		messages = from;
		this.mc = mc;
		new CMD_REQ_Analyzer();
	}

	/**
	 * next_movement labels the next movement the elevator to do -1: nothing 0:
	 * open 1:move up 2: move down
	 */
public synchronized int need_start() {
		
		if (state == STOP)
			return -1;
		if (state == UP && hasUp()) {
			mc.setStatus(elevatorID, currentFloor, state);
			return 1;
		} else if (state == DOWN && hasDown()) {
			mc.setStatus(elevatorID, currentFloor, state);
			return 2;
		}
		// the next check when the state is idle
		int ret = -1;
		for (int i = 1; i < 11; i++) {
			// is there any cmd
			if (innerRequest[i]) {
				if (i < currentFloor)
					ret = state = DOWN;
				else if (i == currentFloor) {
					ret = state = IDLE;
					innerRequest[currentFloor] = false;
					System.err.println("ERROR IN NEED_START!");
				} else
					ret = state = UP;
				mc.setStatus(elevatorID, currentFloor, state);
				return ret;
			}
		}
		for (int i = 1; i < 11; i++) {
			// is there any require
			if (outerDown[i] || outerUp[i]) {
				if (i < currentFloor) {
					ret = state = DOWN;
					if (outerDown[currentFloor]) {
						outerDown[currentFloor] = false;
						ret = 0;
					}
				} else if (i == currentFloor) {
					ret = 0;
					if (outerDown[currentFloor]) {
						state = DOWN;
						outerDown[i] = false;
					} else {
						state = UP;
						outerUp[i] = false;
					}
				} else
					ret = state = UP;
				mc.setStatus(elevatorID, currentFloor, state);
				return ret;
			}
		}
		// if neither cmd nor require ,set the state idle,elevator do nothing
		state = IDLE;
		mc.setStatus(elevatorID, currentFloor, IDLE);
		return ret;
	}

	/** whether or not there is cmd or require from upper */
	private boolean hasUp() {
		for (int i = currentFloor + 1; i < 11; i++)
			if (outerUp[i] || outerDown[i] || innerRequest[i])
				return true;
		return false;
	}

	/** whether or not there is cmd or require from down. */
	private boolean hasDown() {
		for (int i = 1; i < currentFloor; i++)
			if (outerUp[i] || outerDown[i] || innerRequest[i])
				return true;
		return false;
	}

	/**
	 * when the elevator is getting to the next floor, ask for the message to
	 * stop or not
	 */
	public synchronized boolean need_open_door(int[] a) {
		// change the elevator itself
		if (state == STOP) {
			a[0] = 0;
			return true;
		}
		boolean will_open_door = false;
		a[0] = THREE;
		if (state == UP) {
			++currentFloor;
			if (outerUp[currentFloor]) {
				outerUp[currentFloor] = false;
				mc.setStatus(elevatorID, currentFloor, state);
				if (innerRequest[currentFloor]) {
					a[0] = SIX;
					innerRequest[currentFloor] = false;
				}
				return true;
			} else if (innerRequest[currentFloor]) {
				innerRequest[currentFloor] = false;
				if (hasUp()) {
					state = UP;
				} else if (outerDown[currentFloor]) {
					outerDown[currentFloor] = false;
					a[0] = SIX;
					state = DOWN;
				} else if (hasDown()) {
					state = DOWN;
				} else
					state = IDLE;
				mc.setStatus(elevatorID, currentFloor, state);
				return true;
			} else if (hasUp()) {
				state = UP;
				mc.setStatus(elevatorID, currentFloor, state);
				return false;
			} else if (outerDown[currentFloor]) {
				outerDown[currentFloor] = false;
				state = DOWN;
			} else if (hasDown()) {
				state = DOWN;
			} else
				state = IDLE;
			mc.setStatus(elevatorID, currentFloor, state);
			return true;
		}

		else if (state == DOWN) {
			--currentFloor;
			if (outerDown[currentFloor]) {
				outerDown[currentFloor] = false;
				mc.setStatus(elevatorID, currentFloor, state);
				if (innerRequest[currentFloor]) {
					a[0] = SIX;
					innerRequest[currentFloor] = false;
				}
				return true;
			} else if (innerRequest[currentFloor]) {
				innerRequest[currentFloor] = false;
				if (hasDown()) {
					state = DOWN;
				} else if (outerUp[currentFloor]) {
					outerUp[currentFloor] = false;
					a[0] = SIX;
					state = UP;
				} else if (hasUp()) {
					state = UP;
				} else
					state = IDLE;
				mc.setStatus(elevatorID, currentFloor, state);
				return true;
			} else if (hasDown()) {
				state = DOWN;
				mc.setStatus(elevatorID, currentFloor, state);
				return false;
			} else if (outerUp[currentFloor]) {
				outerUp[currentFloor] = false;
				state = UP;
			} else if (hasUp()) {
				state = UP;
			} else
				state = IDLE;
			mc.setStatus(elevatorID, currentFloor, state);
			return true;
		}
		System.err.println("ERROR");
		return will_open_door;
	}

	/**
	 * emergency when user clicked the Emergency button
	 */
	private void emergency() {
		if (state != STOP)
			new Thread() {
				public void run() {
					URL music = getClass().getResource("alarm.wav");
					while (true) {
						AudioClip b = Applet.newAudioClip(music);
						b.play();
						try {
							Thread.sleep(2300);
						} catch (InterruptedException e) {
						}
						if (state == STOP)
							break;
						yield();
					}
				}
			}.start();
	}

	/**
	 * analyze the instructions received from the message center
	 */
	private synchronized void analyze(Message a) {
		int floor;
		switch (a.getType()) {
		case ADMIN:
			switch (a.getNumber()) {
			case 0:// START
				currentFloor = elevator.getCurrentFloor();
				state = IDLE;
				mc.setStatus(0, currentFloor, 3);
				break;
			case 1:// STOP
				mc.setStatus(elevatorID, currentFloor, IDLE);
				state = STOP;
				for (int i = 1; i < 11; i++) {
					innerRequest[i] = outerUp[i] = outerDown[i] = false;
				}
				mc.setStatus(0, currentFloor, -1);
				break;

			case 2:
				mc.autoPlay();
				break;
			case 3:
				mc.cancelAutoPlay();
			}
			break;
		case OPEN:
			elevator.open(THREE);
			break;
		case CLOSE:
			elevator.close();
			break;
		case EMERGENCY:
			emergency();
			mc.setStatus(0, currentFloor, -2);
			break;
		case STOP:
			break;
		case INNER:
			floor = a.getNumber();
			if (floor == currentFloor) {
				System.err.println("ERROR IN ANALYZE!");
				break;
			}
			innerRequest[floor] = true;
			break;
		case OUTER_DOWN:
			floor = a.getNumber();
			outerDown[floor] = true;
			break;
		case OUTER_UP:
			floor = a.getNumber();
			outerUp[floor] = true;
			break;
		default:
			System.out.println("end analyze");
		}
	}

	/**
	 * this class will get the message from the controler and analyze the
	 * message and cmd and req needed to add the message from servers into one
	 * of the six queues. this methord according to analyze() of elevator
	 */

	private class CMD_REQ_Analyzer extends Thread {

		public CMD_REQ_Analyzer() {
			this.start();
		}

		public void run() {
			while (true) {
				Message m = messages.dequeue();
				analyze(m);
			}
		}
	}
}
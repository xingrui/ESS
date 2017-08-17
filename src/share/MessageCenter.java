package src.share;

import src.panels.*;

/** The message exchange center that dispacth all the messages. */
public class MessageCenter {
	private InnerPanel[] in;

	private OuterPanel[] outer;

	private AdminPanel admin;

	private int preFloor = 1;

	private int preState = 0;// used to store the previous state to do the

	// right decision.

	private boolean isAuto;

	private Autoplay autoplay;

	public MessageCenter(InnerPanel[] in, OuterPanel out[], AdminPanel admin) {
		this.in = in;
		this.outer = out;
		this.admin = admin;
	}

	/** When the message center get an auto play message. */
	public void autoPlay() {
		if (!isAuto)
			autoplay = new Autoplay(in, outer);
		isAuto = true;
	}

	/** Cancel auto play. */
	public void cancelAutoPlay() {
		if (isAuto) {
			autoplay.stop();
		}
		isAuto = false;
	}

	/** dispatch the messages to the right destinations. */
	public void setStatus(int elevatorID, int currentFloor, int state) {
		try {
			// change the state to status for writing convenience.
			String status = "";
			switch (state) {
			case -2:
				admin.emergency(elevatorID);
				return;
			case -1:
				status = "stop";
				break;
			case 0:
				status = "idle";
				break;
			case 1:
				status = "up";
				break;
			case 2:
				status = "down";
				break;
			default:
				status = "start";
			}
			admin.setStatus(elevatorID, currentFloor, status);
			in[elevatorID].updateStatus(currentFloor, status);
			if (status.equals("start")) {
				in[elevatorID].setAble(true);
				for (int i = 0; i < 10; i++) {
					if (i + 1 != currentFloor)
						in[elevatorID].addListener(i + 1);
					outer[i].addListener(true);
					outer[i].addListener(false);
					outer[i].setAble(true);
				}
				return;
			} else if (status.equals("stop")) {
				in[elevatorID].setAble(false);
				for (int i = 0; i < 10; i++) {
					in[elevatorID].addListener(i + 1);
					in[elevatorID].removeListener(i + 1);
					outer[i].addListener(true);
					outer[i].removeListener(true);
					outer[i].addListener(false);
					outer[i].removeListener(false);
					outer[i].setAble(false);
				}
				return;
			}
			if (isAuto) {
				autoplay.comeIn(currentFloor);
			}
			if (preFloor != currentFloor)
				in[elevatorID].addListener(preFloor);
			if (preState == 1) {
				outer[preFloor - 1].addListener(true);
			} else if (preState == 2) {
				outer[preFloor - 1].addListener(false);
			}
			if (state == 1) {
				outer[currentFloor - 1].addListener(true);
				outer[currentFloor - 1].removeListener(true);
			} else if (state == 2) {
				outer[currentFloor - 1].addListener(false);
				outer[currentFloor - 1].removeListener(false);
			}
			for (int i = 0; i < 10; i++) {
				outer[i].updateStatus(elevatorID, currentFloor, status);
			}
			if (preFloor != currentFloor) {
				in[elevatorID].addListener(currentFloor);
				in[elevatorID].removeListener(currentFloor);
			}
			preState = state;
			preFloor = currentFloor;
		} catch (NullPointerException e) {
		}
	}
}

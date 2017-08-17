package src.database;

import java.util.Vector;

/**
 * This class process all the method of update require and delete of the data in
 * the database.
 */
public interface Database {

    /** Getting records of elevator by elevatorID. */
    public Vector<String> getRecords(int elevatorID);

    /** Judge whether a person is Administrator or not. */
    public boolean isAdmin(String a, String b);

    /** Add a outer request to database. */
    public void addFloorRequest(int a, String type);

    /** Get the outer request of the elevator system. */
    public Vector<String> getFloorRequest();

    /** Add a inner request to database. */
    public void addControlRecords(int elevatorID,
                                  String event);

    /** Get the currentStatus of the elevator by elevatorID. */
    public String getStatus(int elevatorID);

    /** Update the status of the elevator by elevatorID. */
    public void updateStatus(int i, String status,
                             int currentFloor);
}

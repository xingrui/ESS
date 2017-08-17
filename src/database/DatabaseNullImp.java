package src.database;

import src.database.Database;
import java.util.Vector;

/**
 * This class process all the method of update require and delete of the data in
 * the database.
 */
public class DatabaseNullImp implements Database {
    private static final Database imp = new DatabaseNullImp();
    public static Database getInstance()
    {
        return imp;
    }
    private DatabaseNullImp()
    {
    }

    /** Getting records of elevator by elevatorID. */
    synchronized public Vector<String> getRecords(int elevatorID)
    {
        Vector<String> v = new Vector<String>();
        return v;
    }

    /** Judge whether a person is Administrator or not. */
    synchronized public boolean isAdmin(String a, String b)
    {
        return true;
    }

    /** Add a outer request to database. */
    synchronized public void addFloorRequest(int a, String type)
    {
    }

    /** Get the outer request of the elevator system. */
    synchronized public Vector<String> getFloorRequest()
    {
        Vector<String> v = new Vector<String>();
        return v;
    }

    /** Add a inner request to database. */
    synchronized public void addControlRecords(int elevatorID,
            String event)
    {
    }

    /** Get the currentStatus of the elevator by elevatorID. */
    synchronized public String getStatus(int elevatorID)
    {
        String s = "";
        return s;
    }

    /** Update the status of the elevator by elevatorID. */
    synchronized public void updateStatus(int i, String status,
                                          int currentFloor)
    {
    }
}

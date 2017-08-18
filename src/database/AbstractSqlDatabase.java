package src.database;

import java.sql.*;
import java.util.Vector;
import src.database.Database;

/**
 * This class process all the method of update require and delete of the data in
 * the database.
 */
public abstract class AbstractSqlDatabase implements Database {
    protected Statement stmt;

    /** Getting records of elevator by elevatorID. */
    synchronized public Vector<String> getRecords(int elevatorID)
    {
        String sql = "select * from control where elevatorID = " + elevatorID;
        Vector<String> v = new Vector<String>();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            ResultSetMetaData md = rs.getMetaData();

            int col = md.getColumnCount();
            String s = "";

            for (int i = 1; i <= col; i++) {
                s += md.getColumnName(i) + "\t";

                if (i == 2)
                    s += "\t";
            }

            v.add(s);

            while (rs.next()) {
                s = "";

                for (int i = 1; i <= col; i++) {
                    s += rs.getString(i) + "\t";
                }

                v.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return v;
    }

    /** Judge whether a person is Administrator or not. */
    synchronized public boolean isAdmin(String a, String b)
    {
        String sql = "select * from admin where adminname = \'" + a
                     + "\'and password='" + b + "'";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /** Add a outer request to database. */
    synchronized public void addFloorRequest(int a, String type)
    {
        String sql = "insert into request values(" + a + ",'" + type + "','"
                     + new Date(System.currentTimeMillis()) + " "
                     + new Time(System.currentTimeMillis()) + "')";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error in addFloorRequest");
            e.printStackTrace();
        }
    }

    /** Get the outer request of the elevator system. */
    synchronized public Vector<String> getFloorRequest()
    {
        Vector<String> v = new Vector<String>();
        String sql = "select * from request ";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            ResultSetMetaData md = rs.getMetaData();

            int col = md.getColumnCount();
            String s = "";

            for (int i = 1; i <= col; i++) {
                s += md.getColumnName(i) + "\t";
            }

            v.add(s);

            while (rs.next()) {
                s = "";

                for (int i = 1; i <= col; i++) {
                    s += rs.getString(i) + "\t";
                }

                v.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return v;
    }

    /** Add a inner request to database. */
    synchronized public void addControlRecords(int elevatorID,
            String event)
    {
        String sql = "insert into control values(" + elevatorID + ",'"
                     + new Date(System.currentTimeMillis()) + " "
                     + new Time(System.currentTimeMillis()) + "','" + event + "')";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Get the currentStatus of the elevator by elevatorID. */
    synchronized public String getStatus(int elevatorID)
    {
        String sql = "select status from status where elevatorID = "
                     + elevatorID;
        String s = "";

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                s += rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return s;
    }

    /** Update the status of the elevator by elevatorID. */
    synchronized public void updateStatus(int i, String status,
                                          int currentFloor)
    {
        String sql = "update status set status='" + status + "',currentfloor="
                     + currentFloor + " where elevatorID = " + i;

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

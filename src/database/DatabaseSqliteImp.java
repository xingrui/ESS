package src.database;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import src.database.AbstractSqlDatabase;

/**
 * This class process all the method of update require and delete of the data in
 * the database.
 */
public class DatabaseSqliteImp extends AbstractSqlDatabase {
    private static final Database imp = new DatabaseSqliteImp();
    public static Database getInstance()
    {
        return imp;
    }
    private DatabaseSqliteImp()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("success loading sqlite Driver....");
            String url = "jdbc:sqlite:test.db";
            Connection con = DriverManager.getConnection(url);
            stmt = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                                          "Error loading sqlite Driver....");
        }
    }
}

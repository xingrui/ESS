package src.database;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import src.database.AbstractSqlDatabase;

/**
 * This class process all the method of update require and delete of the data in
 * the database.
 */
public class DatabaseMysqlImp extends AbstractSqlDatabase {
    private static final Database imp = new DatabaseMysqlImp();
    public static Database getInstance()
    {
        return imp;
    }
    private DatabaseMysqlImp()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("success loading mysql Driver....");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                                          "Error loading mysql Driver....");
        }

        try {
            String url = "jdbc:mysql://localhost/ess";
            Connection con = DriverManager.getConnection(url, "root", "123456");
            stmt = con.createStatement();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                                          "Your Database password may not right,"
                                          + "or you haven't create database ess!"
                                          + "Please Check it");
            System.exit(0);
        }
    }
}

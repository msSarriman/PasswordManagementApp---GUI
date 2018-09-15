package secproj;

import java.sql.*;
import javax.swing.*;

public class ConnectionStatus {
    Connection conn=null;
    public static Connection ConnectDb() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Secproj", "root", "myPassword");
            JOptionPane.showMessageDialog(null, "Connection to MySQL server/Secproj Established Successfully!");
            return conn;
           }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error connecting to DB", 0);
            return null;
        }
    }
    public static Connection ConnectDb(String input) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Secproj", "root", "myPassword");
            //JOptionPane.showMessageDialog(null, "Connection to MySQL server/Secproj Established Successfully!");
            return conn;
           }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error connecting to DB", 0);
            return null;
        }
    }
    public void finalize(){
        try{
            conn.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error closing the connection to DB", 0);
        }
    }
}

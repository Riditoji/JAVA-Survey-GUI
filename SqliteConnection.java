import java.sql.*;
import javax.swing.*;

public class SqliteConnection {
 
 static Connection conn = null;
 
 public static Connection dbConnector() {
  
  try {
   // get the JDBC class from JAR
   Class.forName("org.sqlite.JDBC");
   
   // connect to the DB
   conn = DriverManager.getConnection("jdbc:sqlite:c:\\sqlite\\test.db");
   
   // inform user!
   JOptionPane.showMessageDialog(null,  "Connected to DB!");
   
   return conn;
     
  } catch (Exception e) {
   
   JOptionPane.showMessageDialog(null,  "Error Connecting to DB!");
   return null; 
  }
 }

}// end of class
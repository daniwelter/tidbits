package uk.ac.ebi.fgpt.SSLoader;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
   

    /*establish a connection to the database*/
    public static Connection getConnection() throws SQLException, IOException, Exception{

        Properties configFile = new Properties();
        System.out.println("Loading database connection properties...");
        configFile.load(DBConnection.class.getClassLoader().getResourceAsStream("dbcon.properties"));
        System.out.println("Loaded connection properties OK!");

        String driver = configFile.getProperty("DB_DRIVER");
        String dburl = configFile.getProperty("DB_URL");
        String user = configFile.getProperty("USER");;
        String password = configFile.getProperty("PASSWORD");;

        try{
            Class.forName(driver).newInstance();
        }
        catch (Exception ex){
            System.out.println("Database driver not found" + ex);
            throw new Exception("Database driver not found" , ex);
        }

        // @//machineName:port:SID,   userid,  password
        return DriverManager.getConnection(dburl, user, password);
    }
}

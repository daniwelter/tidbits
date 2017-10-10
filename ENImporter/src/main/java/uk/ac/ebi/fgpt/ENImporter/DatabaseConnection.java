package uk.ac.ebi.fgpt.ENImporter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 14/05/13
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseConnection {

    /*establish a connection to the database*/
    public static Connection getConnection() throws SQLException, IOException, Exception{

        System.out.println("Loading database connection properties...");
        System.out.println("Loaded connection properties OK!");

        String driver = "oracle.jdbc.OracleDriver";
        String dburl = "jdbc:oracle:thin:@todd.ebi.ac.uk:1521:ae2tst";
        String user = "gwas";
        String password = "gwa5d6";

        try{
            Class.forName(driver).newInstance();
        }
        catch (Exception ex){
            throw new Exception("Database driver not found" , ex);
        }

        // @//machineName:port:SID,   userid,  password
        return DriverManager.getConnection(dburl, user, password);
    }
}


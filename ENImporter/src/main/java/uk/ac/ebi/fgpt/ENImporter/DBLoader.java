package uk.ac.ebi.fgpt.ENImporter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 14/05/13
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class DBLoader {

    private Connection conn;


    public DBLoader() throws Exception {
        try{
            conn = DatabaseConnection.getConnection();
        }

        catch (SQLException ex) {
            throw new Exception("Attempting database access has generated an SQL exception", ex);
        }
        catch (IOException ex){
            throw new Exception("Attempting database access has resulted in an IO exception", ex);
        }

    }

    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void uploadRecords(ArrayList<Record> records) throws Exception{
        try{
            PreparedStatement writeToDB;
            String query = "INSERT INTO notgwasstudies (PMID, AUTHOR, STUDYDATE, PUBLICATION, LINKTITLE)" +
                    " values (?, ?, ?, ?, ?)";
            writeToDB = conn.prepareStatement(query);

            for(Record rec : records){
                writeToDB.setString(1, rec.getPmid());

                if(rec.getAuthor().equals("")){
                    String foo = "x";
                    writeToDB.setString(2, foo);
                }
                else{
                    writeToDB.setString(2, rec.getAuthor());
                }

                if(rec.getStudydate() == null){
                    writeToDB.setDate(3, null);
                }
                else{
                    writeToDB.setDate(3, rec.getStudydate());
                }
                writeToDB.setString(4, rec.getPublication());
                writeToDB.setString(5, rec.getTitle());
                writeToDB.executeUpdate();

            }




        }

        catch (SQLException ex) {
            throw new Exception("Attempting to write to the database has generated an SQL exception", ex);
        }

    }
}

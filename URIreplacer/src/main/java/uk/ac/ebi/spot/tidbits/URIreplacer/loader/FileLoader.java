package uk.ac.ebi.spot.tidbits.URIreplacer.loader;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dwelter on 18/05/17.
 */
public class FileLoader {

    public Map<String, String> loadTerms(String filename){
        Map<String, String> terms = new HashMap<>();
        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader(filename));
            System.out.println("Opened file " + filename);
        }

        catch (FileNotFoundException fnfe)
        {
            System.out.println("Error opening file '" + filename + "'");
        }
        boolean done = false;

        while(!done){
            try{
                String entry = reader.readLine();
//                 System.out.println(entry);
                if (entry != null){
                    String[] elements = entry.split("\t");
                    terms.put(elements[0], elements[1]);
                }
                else{
                    done = true;
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println("Loaded " + terms.keySet().size() + " terms");
        return terms;
    }
}

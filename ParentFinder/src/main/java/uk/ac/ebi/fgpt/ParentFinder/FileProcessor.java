package uk.ac.ebi.fgpt.ParentFinder;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 26/11/12
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class FileProcessor {

    public void outputToFile(ArrayList<Mapping> allNames, String filename){

        try{
            FileWriter writer = new FileWriter(filename);

            System.out.println("Created file " + filename);


            for(Mapping term : allNames){
                StringBuilder newline = new StringBuilder();
                newline.append(term.getChildName() + "\t");
                newline.append(term.getChildURI() + "\t");
                newline.append(term.getParentName() + "\t");
                newline.append(term.getParentURI() + "\n");
                writer.write(newline.toString());
            }

            writer.close();
            System.out.println("File writer finished");
        }

        catch(IOException ex){
            ex.printStackTrace();
        }

    }

    public ArrayList<Mapping> loadTerms(String filename){
        ArrayList<Mapping> terms = new ArrayList<Mapping>();
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

                if (entry != null){
                    String[] elements = entry.split(";");
                    terms.add(new Mapping(elements[0], elements[1]));
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

        return terms;
    }
}

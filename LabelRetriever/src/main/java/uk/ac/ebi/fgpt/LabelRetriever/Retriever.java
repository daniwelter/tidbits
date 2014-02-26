package uk.ac.ebi.fgpt.LabelRetriever;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 09/08/12
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class Retriever {

    public static void main(String[] args){

        String fileName = "/home/dwelter/Documents/GWAS db/EFO_URI3.txt";

        ArrayList<String> uris = loadTerms(fileName);

        HashMap<String, String> classLabels = new Reader().getLabels(uris);

        String outputFile = "/home/dwelter/Documents/GWAS db/URI-label3.txt";

        outputToFile(classLabels, outputFile);

        System.out.println("Done");
    }


    public static ArrayList<String> loadTerms(String filename){
        ArrayList<String> terms = new ArrayList<String>();
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
                    terms.add(entry);
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

    public static void outputToFile(HashMap<String, String> allNames, String filename){

        try{
            FileWriter writer = new FileWriter(filename);

            System.out.println("Created file " + filename);

            Set<String> keys = allNames.keySet();

            for(String name : keys){
                StringBuilder newline = new StringBuilder();
                newline.append(name + "\t" + allNames.get(name) + "\n");
                writer.write(newline.toString());
            }

            writer.close();
            System.out.println("File writer finished");
        }

        catch(IOException ex){
            ex.printStackTrace();
        }

    }


}

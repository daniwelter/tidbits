package uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.service;

import uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.exception.DispatcherException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dwelter on 27/03/14.
 */
public class AnnotationRetriever {
    private AnnotatorDispatcherService dispatcherService;
    private String inputFile;

    public void setDispatcherService(AnnotatorDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    public AnnotatorDispatcherService getDispatcherService() {
        return dispatcherService;
    }




    public void dispatchAnnotator(){
        Collection<String> cellData = importData();

        try {
            getDispatcherService().dispatchAnnotatorQuery(cellData);
            System.out.println("Annotation retrieval complete");
    //        outputResults(results);

        } catch (DispatcherException e) {
            e.printStackTrace();
        }
    }


    public Collection<String> importData(){
        Collection<String> terms = new ArrayList<String>();
        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader(inputFile));
            System.out.println("Opened file " + inputFile);
        }

        catch (FileNotFoundException fnfe)
        {
            System.out.println("Error opening file '" + inputFile + "'");
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

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFile() {
        return inputFile;
    }

//    public void outputResults(List<BPAnnotation> data){
//        for(BPAnnotation annotation: data){
//            String id = annotation.getAnnotatedClass().get_id();
//            String ontology = annotation.getAnnotatedClass().getLinks().getOntology();
//            int from = annotation.getAnnotations().get(0).getFrom();
//            int to = annotation.getAnnotations().get(0).getTo();
//            String matchType = annotation.getAnnotations().get(0).getMatchType();
//            String text = annotation.getAnnotations().get(0).getText();
//
//            System.out.println(id + "\t" + ontology+ "\t" + from+ "\t" + to + "\t" + matchType + "\t" + text);
//        }
//    }
}

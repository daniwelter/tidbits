package uk.ac.ebi.fgpt.ParentFinder;

import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class CoreLoader
{
    public static void main( String[] args )
    {
        OWLOntology efo = new OntologyLoader().loadOntology();

        FileProcessor processor = new FileProcessor();

//        ClassRetriever retriever = new ClassRetriever(efo);
//
//        ArrayList<String> allNames = retriever.getNames();
//
//
        String inputFile = "/home/dwelter/Documents/Mappings/efo-uri0912.txt";
        String outputFile = "/home/dwelter/Documents/Mappings/parentClasses0912.txt";

        ArrayList<Mapping> mappings = processor.loadTerms(inputFile);

       new ParentFinder(mappings, efo);

        processor.outputToFile(mappings, outputFile);

    }


}

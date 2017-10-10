package uk.ac.ebi.fgpt.LabelRetriever;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 09/08/12
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class Reader {

    private OWLOntology efo = null;
    private OWLOntologyManager manager;

    public Reader(){

        efo = loadOntology();
    }

    public HashMap<String, String> getLabels(ArrayList<String> uris){

       Set<OWLClass> allClasses = efo.getClassesInSignature();
        System.out.println("Number of classes: " + allClasses.size());
        HashMap<String, String> labels = new HashMap<String, String>();
        OWLDataFactory datafactory = manager.getOWLDataFactory();
        OWLAnnotationProperty label_prop = datafactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());


        for(OWLClass cls : allClasses){


            if(uris.contains(cls.getIRI().toString())){
                String uri = cls.getIRI().toString();
                System.out.println("URI " + uri + " is on the list");
                String label = null;

                for (OWLAnnotation annotation : cls.getAnnotations(efo, label_prop)) {
                    if (annotation.getValue() instanceof OWLLiteral) {
                        OWLLiteral val = (OWLLiteral) annotation.getValue();
                        label = val.getLiteral().toLowerCase();
                        labels.put(uri,label);
                    }
                    if(cls.getAnnotations(efo, label_prop).size() != 1){
                        System.out.println("More than one label for class " + label);
                    }
                }
            }
        }

        return labels;
    }

    public OWLOntology loadOntology(){
        System.setProperty("entityExpansionLimit", "100000000");

        System.out.println("Trying to load the ontology");

        String fileName = "/home/dwelter/EFO_SVN/ExFactorInOWL/releasecandidate/efo_release_candidate.owl";


        try {
            // Get hold of an ontology manager
            manager = OWLManager.createOWLOntologyManager();

            // Let's load an ontology from the web
            IRI iri = IRI.create("http://www.ebi.ac.uk/efo/");

            File file = new File(fileName);
            manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(file)));

            efo = manager.loadOntology(iri);

            System.out.println("Loaded ontology: " + efo);

        }


        catch (OWLOntologyCreationIOException e) {
            // IOExceptions during loading get wrapped in an OWLOntologyCreationIOException
            IOException ioException = e.getCause();
            if (ioException instanceof FileNotFoundException) {
                System.out.println("Could not load ontology. File not found: " + ioException.getMessage());
            }
            else if (ioException instanceof UnknownHostException) {
                System.out.println("Could not load ontology. Unknown host: " + ioException.getMessage());
            }
            else {
                System.out.println("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
            }
        }
        catch (UnparsableOntologyException e) {
            // If there was a problem loading an ontology because there are syntax errors in the document (file) that
            // represents the ontology then an UnparsableOntologyException is thrown
            System.out.println("Could not parse the ontology: " + e.getMessage());
            // A map of errors can be obtained from the exception
            Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
            // The map describes which parsers were tried and what the errors were
            for (OWLParser parser : exceptions.keySet()) {
                System.out.println("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
                System.out.println("Failed because: " + exceptions.get(parser).getMessage());
            }
        }
        catch (UnloadableImportException e) {
            // If our ontology contains imports and one or more of the imports could not be loaded then an
            // UnloadableImportException will be thrown (depending on the missing imports handling policy)
            System.out.println("Could not load import: " + e.getImportsDeclaration());
            // The reason for this is specified and an OWLOntologyCreationException
            OWLOntologyCreationException cause = e.getOntologyCreationException();
            System.out.println("Reason: " + cause.getMessage());
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Could not load ontology: " + e.getMessage());
        }

        return efo;
    }



}

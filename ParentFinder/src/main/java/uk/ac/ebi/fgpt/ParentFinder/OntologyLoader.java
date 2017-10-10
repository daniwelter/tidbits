package uk.ac.ebi.fgpt.ParentFinder;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 26/11/12
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class OntologyLoader {
    private OWLOntology efo = null;
    private OWLOntologyManager manager;

    public OWLOntology loadOntology(){
        System.setProperty("entityExpansionLimit", "100000000");

        System.out.println("Trying to load the ontology");

        String fileName = "/home/dwelter/EFO_SVN/ExFactorInOWL/releasecandidate/efo_release_candidate.owl";

//		String fileName = "/home/dwelter/Documents/GWAS Ontology/GWAS-EFO/Chebi/chebi.owl";


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

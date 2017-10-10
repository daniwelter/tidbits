package uk.ac.ebi.spot.tidbits.URIreplacer.loader;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 26/11/12
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class OntologyLoader {
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLClass obsoleteRoot;
    private OWLAnnotationProperty replacedBy;

    public OntologyLoader(){
        this.manager = OWLManager.createOWLOntologyManager();
        this.dataFactory = manager.getOWLDataFactory();
        this.obsoleteRoot = dataFactory.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/obsolete_class"));
        this.replacedBy = dataFactory.getOWLAnnotationProperty(IRI.create("http://purl.obolibrary.org/obo/IAO_0100001"));

    }

    public OWLOntology loadOntology(){
        System.setProperty("entityExpansionLimit", "100000000");

        System.out.println("Trying to load the ontology");

        String fileName = "/home/dwelter/Ontologies/Ancestry/ancestro/ancestro.owl";

//		String fileName = "/home/dwelter/Documents/GWAS Ontology/GWAS-EFO/Chebi/chebi.owl";

        OWLOntology ontology = null;

        try {
            // Get hold of an ontology manager

            // Let's load an ontology from the web
            IRI iri = IRI.create("http://purl.obolibrary.org/obo/ancestro.owl");

            File file = new File(fileName);
            manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(file)));


            ontology = manager.loadOntology(iri);

            System.out.println("Loaded ontology: " + ontology);

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

        return ontology;
    }



    public void saveOntology(OWLOntology ontology){
        try {
            File file = File.createTempFile("ancestro_refactored", "owl", new File("/home/dwelter/Ontologies/Ancestry/"));
            manager.saveOntology(ontology, IRI.create(file.toURI()));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public OWLOntology replaceURIs(Map<String, String> mappings, OWLOntology ontology) {

        Set<String> labels = mappings.keySet();

        Set<OWLClass> classes = ontology.getClassesInSignature();

        OWLEntityRenamer renamer = new OWLEntityRenamer(manager, Collections.singleton(ontology));
        List<OWLOntologyChange> updates = new ArrayList<OWLOntologyChange>();

        OWLAnnotationProperty label_prop = dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());

        for(OWLClass owlClass : classes){

            for (OWLAnnotation annotation : owlClass.getAnnotations(ontology, label_prop)) {
                if (annotation.getValue() instanceof OWLLiteral) {
                    OWLLiteral val = (OWLLiteral) annotation.getValue();
                    String label = val.getLiteral();

                    if(labels.contains(label)) {
                        updates.addAll(renamer.changeIRI(owlClass.getIRI(), IRI.create(mappings.get(label))));


                        //create new obsolete class with old class' IRI
                        OWLClass obsolete = dataFactory.getOWLClass(owlClass.getIRI());
                        OWLDeclarationAxiom newClass = dataFactory.getOWLDeclarationAxiom(obsolete);
                        updates.add(new AddAxiom(ontology, newClass));

                        //set label of obsolete class to the same as the old class
                        OWLAnnotation annot = dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()), dataFactory.getOWLLiteral("obsolete_".concat(label)));
                        OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(obsolete.getIRI(), annot);
                        updates.add(new AddAxiom(ontology, ax));

                        //what replaces this class
                        OWLAnnotation replaced = dataFactory.getOWLAnnotation(replacedBy, dataFactory.getOWLLiteral(mappings.get(label)));
                        OWLAxiom ar = dataFactory.getOWLAnnotationAssertionAxiom(obsolete.getIRI(), replaced);
                        updates.add(new AddAxiom(ontology, ar));

                        //make the class a subclass of obsolete_class
                        OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(obsolete, obsoleteRoot);
                        updates.add(new AddAxiom(ontology, axiom));

                    }

                }

            }
        }

        manager.applyChanges(updates);
        return ontology;
    }
}

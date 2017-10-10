package uk.ac.ebi.fgpt.RubbishTester;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 02/08/12
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class OWLDateTester {

    public static void main(String[] args){

        System.setProperty("entityExpansionLimit", "100000000");

        System.out.println("Trying to load the ontology");

        String fileName = ("/home/dwelter/Desktop/Knowledgebases/OriginalDataPub/owl-data.owl");

        OWLOntologyManager manager = null;
        OWLOntology efo = null;

        try {
            // Get hold of an ontology manager
            manager = OWLManager.createOWLOntologyManager();

            // Let's load an ontology from the web

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.ebi.ac.uk/efo"),
                    IRI.create("http://bar.ebi.ac.uk:8080/trac/export/head/branches/curator/ExperimentalFactorOntology/ExFactorInOWL/releasecandidate/efo_release_candidate.owl")));

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram"),
                    IRI.create("file:/home/dwelter/Desktop/goci-datapublisher/ontology/gwas-diagram-relaxed.owl")));


            File file = new File(fileName);


            efo = manager.loadOntologyFromOntologyDocument(file);

            System.out.println("Loaded ontology: " + efo);

            OWLDataFactory df = manager.getOWLDataFactory();

            System.out.println("Starting reasoner");
            OWLReasonerFactory factory = new Reasoner.ReasonerFactory();
            ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
            OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);

            OWLReasoner reasoner = factory.createReasoner(efo, config);


            OWLClass study = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00037"));
            OWLDataProperty has_pub_date = df.getOWLDataProperty(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00040"));

            Set<OWLNamedIndividual> set = reasoner.getInstances(study,false).getFlattened();

            ArrayList<Date> allDates = new ArrayList<Date>();

            for(OWLNamedIndividual ind : set){
                System.out.println(ind);

                Set<OWLLiteral> pubdate = ind.getDataPropertyValues(has_pub_date, efo);

                for(OWLLiteral foo : pubdate){
                    System.out.println(foo.getDatatype());

                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+HH:mm");

                    System.out.println(foo.getLiteral().toString());

                    Date pub = formatter.parse(foo.getLiteral().toString());

                    System.out.println(pub);
                    allDates.add(pub);


                }
            }

 //           Collections.sort(allDates);

            int n = allDates.size();
            System.out.println("There are " + n  + " dates");
            Date t = null;
            for(int i = 0; i < n; i++){
                for(int j = 1; j < (n-i); j++){
                    if(allDates.get(j-1).compareTo(allDates.get(j)) > 0){
                        System.out.println(allDates.get(j-1) + "\t" + allDates.get(j));
                      t = allDates.get(j-1);
                        allDates.set(j-1,allDates.get(j));
                        allDates.set(j,t);
                    }

                }
            }


            System.out.println("After sorting:");
           for(Date d : allDates){
                System.out.println(d);
            }
     //      }


    /*        OWLClass association = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00035"));
            OWLDataProperty has_pval = df.getOWLDataProperty(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00036"));

            Set<OWLNamedIndividual> assocs = reasoner.getInstances(association,false).getFlattened();

            ArrayList<Float> pvalues = new ArrayList<Float>();

            for(OWLNamedIndividual a : assocs){
                System.out.println(a);

                Set<OWLLiteral> pvs = a.getDataPropertyValues(has_pval, efo);

                for(OWLLiteral p : pvs){
                    System.out.println(p.getDatatype());

                    System.out.println(p.getLiteral());

                    pvalues.add(p.parseFloat());
                }

            }


            for(Float p : pvalues){
                System.out.println(p);
            }     */

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
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

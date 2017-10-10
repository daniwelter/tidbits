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
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 29/06/12
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class DLQueryTester {

    public static void main(String[] args){

        System.setProperty("entityExpansionLimit", "100000000");

        System.out.println("Trying to load the ontology");

        String fileName = ("/home/dwelter/Desktop/goci-datapublisher/ontology/gwas-diagram-data-2007-12.owl");

        OWLOntologyManager manager = null;
        OWLOntology efo = null;

        try {
            // Get hold of an ontology manager
            manager = OWLManager.createOWLOntologyManager();

            // Let's load an ontology from the web
//           IRI iri = IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/2012/06/11/data");
//
            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.ebi.ac.uk/efo"),
                    IRI.create("http://bar.ebi.ac.uk:8080/trac/export/head/branches/curator/ExperimentalFactorOntology/ExFactorInOWL/releasecandidate/efo_release_candidate.owl" )));

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram"),
                    IRI.create("file:/home/dwelter/Desktop/goci-datapublisher/ontology/gwas-diagram-relaxed.owl")));


            File file = new File(fileName);
     //       manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(file)));
  //          manager.loadOntologyFromOntologyDocument(IRI.create("file:/home/dwelter/Desktop/goci-datapublisher/ontology/gwas-diagram-relaxed.owl"));


            efo = manager.loadOntologyFromOntologyDocument(file);


   //         efo = manager.loadOntology(iri);

            System.out.println("Loaded ontology: " + efo);

            OWLDataFactory df = manager.getOWLDataFactory();

            System.out.println("Starting reasoner");
            OWLReasonerFactory factory = new Reasoner.ReasonerFactory();
            ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
            OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);

            OWLReasoner reasoner = factory.createReasoner(efo, config);

 /*           Set<OWLClass> all = reasoner.getSubClasses(df.getOWLThing(), false).getFlattened();

            System.out.println("Done reasoning");

            OWLDataProperty has_pub_date = df.getOWLDataProperty(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00040"));
            System.out.println(has_pub_date.toStringID());
            OWLDatatype dateTime = df.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTime"));
            System.out.println(dateTime.toStringID());
            OWLLiteral max_date = df.getOWLLiteral("2009-01-01T00:00:00+00:00", dateTime);
            System.out.println(max_date.getLiteral());
            OWLFacet dateFacet = OWLFacet.MAX_EXCLUSIVE;
            OWLDataRange publishedBefore = df.getOWLDatatypeRestriction(dateTime,dateFacet,max_date);

            System.out.println(publishedBefore.toString());

            OWLDataSomeValuesFrom pre2009_studies = df.getOWLDataSomeValuesFrom(has_pub_date, publishedBefore);

            OWLObjectProperty has_part = df.getOWLObjectProperty(IRI.create("http://www.obofoundry.org/ro/ro.owl#has_part"));

    //        OWLObjectPropertyExpression part_of = has_part.getInverseProperty();

     //       OWLObjectProperty part_of = df.getOWLObjectProperty(IRI.create("http://www.obofoundry.org/ro/ro.owl#part_of"));

            OWLObjectSomeValuesFrom part_of_assoc = df.getOWLObjectSomeValuesFrom(has_part, pre2009_studies);


    /*        if(reasoner.getInstances(pre2009_studies,false).getFlattened().size() != 0){
                Set<OWLNamedIndividual> set = reasoner.getInstances(pre2009_studies,false).getFlattened();
                                    for(OWLNamedIndividual ind : set){
                                        System.out.println(ind);
                                    }
            }

            System.out.println(pre2009_studies.toString());

            OWLClass association = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram//EFO_GD00035"));
            OWLObjectIntersectionOf trait_associations = df.getOWLObjectIntersectionOf(association, pre2009_studies);

            System.out.println(trait_associations);     */

//
//            List<OWLAnnotationProperty> properties = Arrays.asList(df.getRDFSLabel());
//            AnnotationValueShortFormProvider annoSFP = new AnnotationValueShortFormProvider(
//                    properties, new HashMap<OWLAnnotationProperty, List<String>>(), manager);
//            ShortFormEntityChecker checker = new ShortFormEntityChecker(new BidirectionalShortFormProviderAdapter(manager, manager.getOntologies(), annoSFP));
//            ManchesterOWLSyntaxClassExpressionParser parser = new ManchesterOWLSyntaxClassExpressionParser(df, checker);
//
//            //String query1 = "chromosome and  has_part value band_10p15.1";
//
//    //        String query1 = "'GWAS study' and  has_publication_date some dateTime[< \"2009-01-01T00:00:00+00:00\"^^dateTime]";
//            String query1 = "has_publication_date some dateTime[< \"2009-01-01T00:00:00+00:00\"^^dateTime]";
//    //        String query2 = "'trait association' and  part_of some (".concat(query3).concat(")");
//
//            OWLClassExpression pre2009 = parser.parse(query1);
//
//
//            OWLClass study = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00037"));
//            OWLClassExpression pre2009_studies = df.getOWLObjectIntersectionOf(study,pre2009);
//
//   /*         System.out.println("Computing relevant studies");
//            if(reasoner.getInstances(pre2009_studies,false).getFlattened().size() != 0){
//                System.out.println("Found some studies");
//                Set<OWLNamedIndividual> set = reasoner.getInstances(pre2009_studies,false).getFlattened();
//                System.out.println("About to print studies");
//                for(OWLNamedIndividual ind : set){
//                    System.out.println(ind);
//                }
//            }             */
//
//            //OWLClassExpression trait_associations = parser.parse(query2);
//
//            OWLObjectProperty part_of = df.getOWLObjectProperty(IRI.create("http://www.obofoundry.org/ro/ro.owl#part_of"));
//            OWLObjectSomeValuesFrom part_of_assoc = df.getOWLObjectSomeValuesFrom(part_of, pre2009_studies);
//
//            OWLClass association = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00035"));
//            OWLClassExpression trait_associations = df.getOWLObjectIntersectionOf(association, part_of_assoc);
//
//
///*             System.out.println("Computing trait associations");
//            if(reasoner.getInstances(trait_associations,false).getFlattened().size() != 0){
//                System.out.println("Found some relevant trait associations");
//                Set<OWLNamedIndividual> set = reasoner.getInstances(trait_associations,false).getFlattened();
//                for(OWLNamedIndividual ind : set){
//                    System.out.println(ind);
//                }
//
//            }*/
//
//            OWLObjectProperty has_about = df.getOWLObjectProperty(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram#EFO_GD00042"));
//            OWLObjectSomeValuesFrom some_snps = df.getOWLObjectSomeValuesFrom(has_about, trait_associations);
//
//            OWLClass snp = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00003"));
//            OWLClassExpression pre2009_snps = df.getOWLObjectIntersectionOf(snp, some_snps);
//
//     /*       System.out.println("Computing SNPs");
//            if(reasoner.getInstances(pre2009_snps,false).getFlattened().size() != 0){
//                System.out.println("Found some relevant SNPs");
//                Set<OWLNamedIndividual> set = reasoner.getInstances(pre2009_snps,false).getFlattened();
//                for(OWLNamedIndividual ind : set){
//                    System.out.println(ind);
//                }
//
//            }*/
//
//
//            OWLObjectProperty location_of = df.getOWLObjectProperty(IRI.create("http://www.obofoundry.org/ro/ro.owl#location_of"));
//            OWLObjectSomeValuesFrom some_bands = df.getOWLObjectSomeValuesFrom(location_of, pre2009_snps);
//
//            OWLClass cyto_band = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00002"));
//            OWLClassExpression pre2009_bands = df.getOWLObjectIntersectionOf(cyto_band, some_bands);
//
//      /*      System.out.println("Computing bands");
//            if(reasoner.getInstances(pre2009_bands,false).getFlattened().size() != 0){
//                System.out.println("Found some relevant bands");
//                Set<OWLNamedIndividual> set = reasoner.getInstances(pre2009_bands,false).getFlattened();
//                for(OWLNamedIndividual ind : set){
//                    System.out.println(ind);
//                }
//            }*/
//
//
//            OWLClassExpression end_result = df.getOWLObjectUnionOf(pre2009_bands, trait_associations);
//
//            System.out.println("Computing all data");
//            if(reasoner.getInstances(end_result,false).getFlattened().size() != 0){
//                System.out.println("Found some relevant bands and associations");
//                Set<OWLNamedIndividual> set = reasoner.getInstances(end_result,false).getFlattened();
//                for(OWLNamedIndividual ind : set){
//                    System.out.println(ind);
//                }
//            }

            OWLClass ef = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/EFO_0000001"));
            OWLClass traitassociation = df.getOWLClass(IRI.create("http://www.ebi.ac.uk/efo/gwas-diagram/EFO_GD00035"));

            System.out.println("Getting stuff from the reasoner");

            Set<OWLNamedIndividual>  allTraits = reasoner.getInstances(ef,false).getFlattened();
            Set<OWLNamedIndividual> allAssocs = reasoner.getInstances(traitassociation,false).getFlattened();

            System.out.println("Got everything");


            for(OWLNamedIndividual ind : allAssocs){
                System.out.println(ind.getIRI().toString());
            }

            System.out.println("Traits: ");
            for(OWLNamedIndividual ind : allTraits){
                if(!allAssocs.contains(ind)){
                    System.out.println(ind.getIRI().toString());
                }
                else{
                    System.out.println("Trait " + ind.getIRI().toString() + " is in fact an association");
                }
            }

                System.out.println("Done");

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
//        catch (ParserException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

    }
}

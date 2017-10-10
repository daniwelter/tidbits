package uk.ac.ebi.fgpt.ParentFinder;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 26/11/12
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
public class ParentFinder {
    private OWLReasoner reasoner;
    private OWLDataFactory df;
//    private HashMap<String, String> mappings;


    public ParentFinder(ArrayList<Mapping> terms, OWLOntology efo){
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
        reasoner = reasonerFactory.createReasoner(efo, config);
        df = OWLManager.getOWLDataFactory();
//        mappings = new HashMap<String, String>();

        for(Mapping term : terms){
            String map = findParent(term.getChildURI());
            term.setParentURI(map);
            term.setParentName(ParentList.PARENT_URI.get(map));
        }

    }

    public String findParent(String term){
        String parent= null;

        OWLClass cls =  df.getOWLClass(IRI.create(term));
        
        Set<OWLClass> parents = reasoner.getSuperClasses(cls, false).getFlattened();
        Set<String> available = ParentList.PARENT_URI.keySet();

        OWLClass leaf = null;
        int largest = 0;

        if(parents.size() == 2){
            System.out.println("Trait " + term + " is not mapped");
        }
        else{
            for (OWLClass t : parents) {
                String iri = t.getIRI().toString();
                int allp = reasoner.getSuperClasses(t, false).getFlattened().size();

                if (allp > largest && available.contains(iri)) {
                    largest = allp;
                    leaf = t;
                }
            }
            if (leaf != null) {
                parent = leaf.getIRI().toString();
            }
            else {
                System.out.println("Could not identify a suitable  parent category for trait " + term);
            }
        }
        return parent;
    }
}

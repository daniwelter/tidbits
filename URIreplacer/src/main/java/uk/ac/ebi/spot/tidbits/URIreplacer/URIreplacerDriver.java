package uk.ac.ebi.spot.tidbits.URIreplacer;

import org.semanticweb.owlapi.model.OWLOntology;
import uk.ac.ebi.spot.tidbits.URIreplacer.loader.FileLoader;
import uk.ac.ebi.spot.tidbits.URIreplacer.loader.OntologyLoader;

import java.util.Map;

/**
 * Created by dwelter on 18/05/17.
 */
public class URIreplacerDriver {

    public static void main( String[] args )
    {
        OntologyLoader ontologyLoader  = new OntologyLoader();

        OWLOntology ontology = ontologyLoader.loadOntology();

        FileLoader loader = new FileLoader();


        String inputFile = "/home/dwelter/Ontologies/Ancestry/label_to_dbpedia.txt";

        Map<String, String> mappings = loader.loadTerms(inputFile);

        OWLOntology refactored = ontologyLoader.replaceURIs(mappings, ontology);

        ontologyLoader.saveOntology(refactored);
    }
}

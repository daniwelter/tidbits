
package uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.model;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class BPAnnotation {

    private AnnotatedClass annotatedClass;
    private List<Object> hierarchy = new ArrayList<Object>();
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private List<Object> mappings = new ArrayList<Object>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    public void setAnnotatedClass(AnnotatedClass annotatedClass) {
        this.annotatedClass = annotatedClass;
    }

    public List<Object> getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(List<Object> hierarchy) {
        this.hierarchy = hierarchy;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Object> getMappings() {
        return mappings;
    }

    public void setMappings(List<Object> mappings) {
        this.mappings = mappings;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

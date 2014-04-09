
package uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.model;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class AnnotatedClass {

    private String _id;
    private String _type;
    private Links links;
    private _context_ _context;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public _context_ get_context() {
        return _context;
    }

    public void set_context(_context_ _context) {
        this._context = _context;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

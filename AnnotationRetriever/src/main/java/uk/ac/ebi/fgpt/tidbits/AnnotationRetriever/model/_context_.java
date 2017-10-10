
package uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.model;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class _context_ {

    private String _vocab;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String get_vocab() {
        return _vocab;
    }

    public void set_vocab(String _vocab) {
        this._vocab = _vocab;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

package uk.ac.ebi.fgpt.ParentFinder;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 26/11/12
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public class Mapping {
    private String childName, childURI, parentName, parentURI;

    public Mapping(String child, String uri){
        childName = child;
        childURI = uri;
    }

    public void setParentName(String name){
        parentName = name;
    }

    public void setParentURI(String uri){
        parentURI = uri;
    }

    public String getChildName(){
        return childName;
    }

    public String getChildURI(){
        return childURI;
    }

    public String getParentName(){
        return parentName;
    }

    public String getParentURI(){
        return parentURI;
    }
}

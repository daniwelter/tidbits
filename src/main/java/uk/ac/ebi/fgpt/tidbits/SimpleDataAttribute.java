package uk.ac.ebi.fgpt.tidbits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwelter on 18/09/14.
 */
public class SimpleDataAttribute {


    private int index;
    private List<String> permissibleStrings;
    private List<String> opplVariables;

    public SimpleDataAttribute(int index){
        this.index = index;
        permissibleStrings = new ArrayList<String>();
        opplVariables = new ArrayList<String>();
    }


    public List<String> getPermissibleStrings() {
        return permissibleStrings;
    }

    public List<String> getOpplVariables(){
        return opplVariables;
    }

    public void addVariable(String opplVariable){
        opplVariables.add(opplVariable);
    }


    public int getIndex() {
        return index;
    }


    public void addString(String String){
        permissibleStrings.add(String);
    }

   
}

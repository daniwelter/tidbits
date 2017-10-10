package uk.ac.ebi.fgpt.svg.path.converter;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 12/03/12
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
public class Converter {
    
    public static void main(String[] args){
        
        String[] chroms = {"2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","Y"};
        
        for(int i = 0; i < chroms.length; i++){
            String in_front = "/home/dwelter/chromosomes/";
            String in_end = ".svg";
            
            String out_front = "/home/dwelter/chromosomes/mod/";
       //     String out_end = "_mod.svg";
            String identifier = chroms[i];

            String input = in_front.concat(identifier).concat(in_end);
            String output = out_front.concat(identifier).concat(in_end);
            
            System.out.println("Processing chromosome " + chroms[i]);

            new FileReader(input, output);
            
        }


    }


}

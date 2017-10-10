package uk.ac.ebi.fgpt.svg.path.addition;


/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 22/03/12
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class Addition {

    public static void main(String[] args){

   //     String[] chroms = {"1","3","4","6","7","8","9","10","11","12","13","14","16","17","18","19","20","21","22"};
        
        String[] chroms = {"2"};


        for(int i = 0; i < chroms.length; i++){
            String in_front = "/home/dwelter/chromosomes/";
            String end = ".svg";

            String out_front = "/home/dwelter/chromosomes/mod/";
            String identifier = chroms[i];

            String input = in_front.concat(identifier).concat(end);
            String output = out_front.concat(identifier).concat(end);

            System.out.println("Processing chromosome " + chroms[i]);

            new SVGExtractor(input, output);

        }


    }
}

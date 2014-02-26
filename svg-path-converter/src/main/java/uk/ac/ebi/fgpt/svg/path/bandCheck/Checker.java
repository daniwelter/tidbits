package uk.ac.ebi.fgpt.svg.path.bandCheck;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 23/04/12
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public class Checker {

    public static void main(String[] args){

        String[] chroms = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"};

        for(int i = 0; i < chroms.length; i++){
            String in_front = "/home/dwelter/goci/goci-diagram/goci-pussycat-renderlet/src/main/resources/chromosomes/";
            String in_end = ".svg";

             String identifier = chroms[i];

            String input = in_front.concat(identifier).concat(in_end);

            System.out.println("Processing chromosome " + chroms[i]);

            Reader reader = new Reader(input);


        }


    }
}

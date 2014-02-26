package uk.ac.ebi.fgpt.svg.path.converter;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 14/03/12
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateCalculator {
    
    public static String calculateCoordinates(String path, double[] transform){
        StringTokenizer tokenizer = new StringTokenizer(path);

        ArrayList<String> pathElements = new ArrayList<String>();
        
        while(tokenizer.hasMoreTokens()){
            pathElements.add(tokenizer.nextToken());            
        }
        

        String firstEl = pathElements.get(0);

        String moveTo = pathElements.get(1);
        String newMoveTo = absTransform(moveTo,transform);

        pathElements.set(1,newMoveTo);

        String current;
        String newCoords;

        if(firstEl.equals("M")){
            for(int i = 2; i < pathElements.size(); i++){
                current = pathElements.get(i);
                if(current.contains(",")){
                     newCoords = absTransform(current,transform);
                    pathElements.set(i,newCoords);
                }
            }
        }     
        else{
            for(int j = 2; j < pathElements.size(); j++){
                current = pathElements.get(j);
                if(current.contains(",")){
                    newCoords = relTransform(current,transform);
                    pathElements.set(j,newCoords);
                }
            }
        }
        
        StringBuilder builder = new StringBuilder();
        for(int k = 0; k < pathElements.size(); k++){
            builder.append(pathElements.get(k));
            builder.append(" ");
        }
        String newPath = builder.toString();

        return newPath;
    }
    
    public static String absTransform(String xy, double[] transform){
        String newxy = null;
        
        String[] coords = xy.split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);

        double newX = ((transform[0]*x) + (transform[2]*y) + (transform[4]));
        double newY = ((transform[1]*x) + (transform[3]*y) + (transform[5]));

        newxy = Double.toString(newX).concat(",").concat(Double.toString(newY));
        return newxy;
    }

    public static String relTransform(String xy, double[] transform){
        String newxy = null;

        String[] coords = xy.split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);

        double newX = ((transform[0]*x) + (transform[2]*y));
        double newY = ((transform[1]*x) + (transform[3]*y));

        newxy = Double.toString(newX).concat(",").concat(Double.toString(newY));
        return newxy;
    }
    
    public static double[] placeLabel(double[] old, double[] matrix){
        double[] newXY = new double[old.length];

        if(old.length == 3){
            newXY[0] = ((matrix[0]*old[0]) + (matrix[2]*old[2]) + (matrix[4]));
            newXY[1] = ((matrix[0]*old[1]) + (matrix[2]*old[2]) + (matrix[4]));
            newXY[2] = ((matrix[1]*old[0]) + (matrix[3]*old[2]) + (matrix[5]));
        }
        else{
            newXY[0] = ((matrix[0]*old[0]) + (matrix[2]*old[1]) + (matrix[4]));
            newXY[1] = ((matrix[1]*old[0]) + (matrix[3]*old[1]) + (matrix[5]));
        }
        return newXY;
    }

    
}

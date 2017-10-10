package uk.ac.ebi.fgpt.svg.path.addition;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 22/03/12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class PathGenerator {
    
    private String newD;
    private ArrayList<String> topPath, bottomPath, newPath;

    
    public PathGenerator(String top, String bottom, int option){
  /*      System.out.println("Top: " +top);
        System.out.println("Bottom: " + bottom);              */

        topPath = getElements(top);
        bottomPath = getElements(bottom);
        calculatePath(option);

        StringBuilder builder = new StringBuilder();
        for(int k = 0; k < newPath.size(); k++){
            builder.append(newPath.get(k));
            builder.append(" ");
        }
        newD = builder.toString();

        if(option == 2){
            System.out.println("Top: " +top);
            System.out.println("Bottom: " + bottom);
            System.out.println("New path: "+ newD);
        }
        
 //       System.out.println("New path: "+ newD);

    }
    
    public ArrayList<String> getElements(String path){
        StringTokenizer tokenizer = new StringTokenizer(path);

        ArrayList<String> pathElements = new ArrayList<String>();

        while(tokenizer.hasMoreTokens()){
            pathElements.add(tokenizer.nextToken());
        }

        return pathElements;
    }

    public void calculatePath(int option){
        String newx;
        double newy=0;
        String txy = topPath.get(1);
        String[] tcoords = txy.split(",");
        double ty = Double.parseDouble(tcoords[1]);

        String bxy = bottomPath.get(1);
        String[] bcoords = bxy.split(",");
        double by = Double.parseDouble(bcoords[1]);

        if(topPath.contains(("c"))){
            newPath = bottomPath;
            newx = bcoords[0];
        }
        else{
            newPath = topPath;
            newx = tcoords[0];
        }

        if(option == 1){
            String bh = bottomPath.get(3);
            String[] coords = bh.split(",");
            double h = Math.abs(Double.parseDouble(coords[1]));
            newy = by- h;
        }
    /*  else if (option == 2){
            String element = bottomPath.get(2);
            String[] coords = element.split(",");
            double temp = Math.abs(Double.parseDouble(coords[1]));
            by = by-temp;

            for(int i = 3; i < bottomPath.size(); i++){
                element = bottomPath.get(i);

                if(element.contains("c")){
                    while(!bottomPath.get(i+1).contains("l")){

                        i = i+3;
                        String point = bottomPath.get(i);
                        coords = point.split(",");
                        temp = Double.parseDouble(coords[1]);
                        if(temp < 0){
                            by = by+temp;
                            System.out.println(by + " " + temp);
                        }
                    }
                }
               else if(element.contains("l")){
                    i = i+1;
                    String point = bottomPath.get(i);
                    coords = point.split(",");
                    temp = Double.parseDouble(coords[1]);
                    if(temp < 0){
                        by = by+temp;
                        System.out.println(by + " " + temp);
                    }
                }
            }
            
            newy = by;
        }             */
        else if (option == 3){
            newy = by;

        }

        double newh = newy-ty;
        String newxy = newx.concat(",").concat(Double.toString(newy));
        newPath.remove(1);
        newPath.add(1,newxy);

        String h1 = "0,".concat(Double.toString(-newh));
        String h2 = "0,".concat(Double.toString(newh));

        newPath.remove(3);
        newPath.add(3,h1);

        newPath.remove(5);
        newPath.add(5,h2);

    }

    public String getNewPath(){
        return newD;
    }
}

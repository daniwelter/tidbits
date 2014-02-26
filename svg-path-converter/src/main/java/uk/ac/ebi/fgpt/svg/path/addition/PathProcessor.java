package uk.ac.ebi.fgpt.svg.path.addition;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 22/03/12
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public class PathProcessor {
    
    private Element g;
    private ArrayList<String> ps, qs;
    private HashMap<String,String> ds, newd;
    private String middle;

    public PathProcessor(Element gblock) {
        g = gblock;
        NodeList paths = g.getElementsByTagName("path");
        extractPaths(paths);
        processPaths();

        Element original = (Element)paths.item(3);
        addPaths(original);

    }

    public void extractPaths(NodeList paths){
        ps = new ArrayList<String>();
        qs = new ArrayList<String>();
        ds = new HashMap<String, String>();

        for(int i = 0; i < paths.getLength(); i++){
            Element path = (Element)paths.item(i);
            String id = path.getAttribute("id");
            String d = path.getAttribute("d");

            if(id.contains("centre") || id.contains("outline") || id.contains("satellite") || id.contains("-o")){
                continue;
            }
            else if (id.contains("centromere")){
                middle = id;
                ds.put(id,d);
            }
            else if (id.contains("p")){
                ps.add(id);
                ds.put(id,d);
            }
            else {
                qs.add(id);
                ds.put(id,d);
            }
        }

        Collections.sort(qs);
        Collections.sort(ps);
        Collections.reverse(ps);
    }

    public void processPaths(){
        newd = new HashMap<String, String>();
        int option;
  //account for chromosomes that don't have any bands at the top
        if(ps.size() != 0){
            for(int j =0; j < ps.size(); j++){
                String top = ds.get(ps.get(j));
                String bottom;
                
                if(j == (ps.size()-1)){
         /*           bottom = ds.get(middle);
                    option = 2;*/
                    String comment = "<!-- tricky centromere, put in manually -->";
                    String id = "cent-1";
                    newd.put(id, comment);
                    continue;
                }
                else{
                    bottom = ds.get(ps.get(j+1));
                    option = 1;
                }

                if(j == 0 && !top.contains("c")){
                    String comment = "<!-- top end, put in manually -->";
                    String id = "top";
                    newd.put(id, comment);

                }

                PathGenerator generator = new PathGenerator(top,bottom, option);
                String white = generator.getNewPath();
                String id = ps.get(j).concat("-1");
                newd.put(id, white);
            }
        }


        for(int k =0; k < qs.size(); k++){
            String top;
            if(k == 0){
                top = ds.get(middle);
            }
            else{
                top = ds.get(qs.get(k-1));
            }

            String bottom = ds.get(qs.get(k));

            if(k == (qs.size()-1) && !bottom.contains("c")){
                String comment = "<!-- bottom end, put in manually -->";
                String id = "bottom";
                newd.put(id, comment);
            }

            if(bottom.contains("c")){
                option = 3;
            }
            else{
                option = 1;
            }

            PathGenerator generator = new PathGenerator(top,bottom,option);
            String white = generator.getNewPath();
            String id = qs.get(k).concat("-1");
            newd.put(id, white);

        }
    }

    public void addPaths(Element original){
            String[] keys = newd.keySet().toArray(new String[0]);

        for(int i =0; i < keys.length; i++){
            Element path = (Element)original.cloneNode(false);

            String style = "fill:none;stroke:#211c1d;stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;display:none";
//            String style = "fill:none;stroke:red;stroke-width:0.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none";

            String id = keys[i];
            String d = newd.get(id);

            path.removeAttribute("style");
            path.setAttribute("style",style);
            path.removeAttribute("d");
            path.setAttribute("d",d);
            path.removeAttribute("id");
            path.setAttribute("id", id);
            
            g.appendChild(path);
        }

    }

    public Element getNewG(){
        return g;
    }
}

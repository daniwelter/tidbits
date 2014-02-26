package uk.ac.ebi.fgpt.svg.path.bandCheck;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 23/04/12
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class Reader {

    public Reader(String filename) {

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        FileInputStream svgstream = null;
        Document doc = null;


        try {
            svgstream = new FileInputStream(filename);
            doc = f.createDocument(" file:///home/dwelter/testFile.svg", svgstream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (doc != null) {
            Element root = doc.getDocumentElement();

            processPaths(root);

        }

     }





    public void processPaths(Element root){
        ArrayList<String> ps = new ArrayList<String>();
        ArrayList<String> qs = new ArrayList<String>();
        Element g =(Element)root.getChildNodes().item(1);

        NodeList paths = g.getElementsByTagName("path");



        for(int i = 0; i < paths.getLength(); i++){
            Element path = (Element)paths.item(i);

            String id = path.getAttribute("id");

            if(id.contains("centre") || id.contains("outline") || id.contains("satellite") || id.contains("-o") || id.contains("centromere")){
                continue;
            }

            else if (id.contains("p")){
                ps.add(id);
            }
            else {
                qs.add(id);
            }

        }
        Collections.reverse(ps);
        printBands(ps);
        Collections.sort(qs);
        printBands(qs);
    }

    public void printBands(ArrayList<String> bands){

        for(String band : bands){
            System.out.println(band);
        }
    }



}

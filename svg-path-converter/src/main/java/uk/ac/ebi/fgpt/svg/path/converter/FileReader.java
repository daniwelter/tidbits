package uk.ac.ebi.fgpt.svg.path.converter;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 12/03/12
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class FileReader {
    
    public FileReader(String inputfile, String outputfile) {

        Document output = extractSVG(inputfile);

        outputNewSVG(output, outputfile);

    }
    

    public Document extractSVG(String filename){

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        FileInputStream svgstream = null;
        Document input = null;

        Document output = createNewDocument();

        try {
            svgstream = new FileInputStream(filename);
            input = f.createDocument(" file:///home/dwelter/testFile.svg", svgstream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

       if (input != null) {
          Element root = input.getDocumentElement();

           output = processPaths(root, output);
       }

        return output;
    }


    public Document processPaths(Element root, Document output){
        Element original =  (Element)root.getChildNodes().item(1);
        Element newroot = output.getDocumentElement();

        Element firstG = (Element)output.importNode(original,true);

  /*     String matrixTrans = firstG.getAttribute("transform");
        matrixTrans = matrixTrans.replace("matrix(","");
        matrixTrans = matrixTrans.replace(")","");

        String[] mFactors = matrixTrans.split(",");
        double[] matrixFactors = new double[mFactors.length];

        for(int i = 0; i < mFactors.length; i++){
            matrixFactors[i] = Double.parseDouble(mFactors[i]);
        }          */

        Element secondG = (Element)firstG.getChildNodes().item(1);

        String scaleTrans = secondG.getAttribute("transform");
   //     scaleTrans = scaleTrans.replace("scale(", "");
        scaleTrans = scaleTrans.replace("translate(", "");
        scaleTrans = scaleTrans.replace(")","");

        String[] sFactors = scaleTrans.split(",");
        double[] scaleFactors = {1,0,0,1,Double.parseDouble(sFactors[0]),Double.parseDouble(sFactors[1])};


/*       Element thirdG = (Element)secondG.getChildNodes().item(1);
        NodeList paths = thirdG.getElementsByTagName("path");
        Element label = (Element)thirdG.getElementsByTagName("g").item(0);                  */

        NodeList paths = secondG.getElementsByTagName("path");
        Element label = (Element)secondG.getElementsByTagName("text").item(0);

        Element newG = output.createElement("g");
        Element newLabel = output.createElement("text");

        ArrayList<Element> newpaths = new ArrayList<Element>();


        for(int k = 0; k < paths.getLength(); k++){
            Element path = (Element)paths.item(k);
            String coordinates = path.getAttribute("d");

   /*         String intermediate = CoordinateCalculator.calculateCoordinates(coordinates, scaleFactors);
            String newPath = CoordinateCalculator.calculateCoordinates(intermediate,matrixFactors);               */

            String newPath = CoordinateCalculator.calculateCoordinates(coordinates, scaleFactors);

            path.removeAttribute("d");
            path.setAttribute("d",newPath);

            newpaths.add(path);
        }

        for(int j = 0; j < newpaths.size(); j++){
            newG.appendChild(newpaths.get(j));
        }

     //   handleLabel(label, newLabel,matrixFactors,scaleFactors);

        handleLabel(label, newLabel,null,scaleFactors);
        
        newG.appendChild(newLabel);
        newroot.appendChild(newG);

        return output;
    }


    public void handleLabel(Element old, Element label, double[] outerMatrix, double[] outerScale){
  /*      String scale = old.getAttribute("transform");
        scale = scale.replace("scale(", "");
        scale = scale.replace(")","");
        String[] scale2 = scale.split(",");
        double[] innerScale = {Double.parseDouble(scale2[0]),0,0,Double.parseDouble(scale2[1]),0,0};

        Element text = (Element)old.getElementsByTagName("text").item(0);
        String matrix = text.getAttribute("transform");
        matrix = matrix.replace("matrix(","");
        matrix = matrix.replace(")","");
        String[] matrix2 = matrix.split(",");

        double[] innerMatrix = new double[matrix2.length];

        for(int i = 0; i < matrix2.length; i++){
            innerMatrix[i] = Double.parseDouble(matrix2[i]);
        }

                
        Element tspan = (Element)text.getElementsByTagName("tspan").item(0);   */

        Element tspan = (Element)old.getElementsByTagName("tspan").item(0);

        String oldX = tspan.getAttribute("x");
        String oldY = tspan.getAttribute("y");
        
        double[] allcoords;

        if(oldX.contains(" ")){
            String[] x2 = oldX.split(" ");
            allcoords = new double[3];
            allcoords[0] = Double.parseDouble(x2[0]);
            allcoords[1] = Double.parseDouble(x2[1]);
            allcoords[2] = Double.parseDouble(oldY);
       }
        else{
            allcoords = new double[2];
            allcoords[0] = Double.parseDouble(oldX);
            allcoords[1] = Double.parseDouble(oldY);
        }

  /*      double[] intermediate = CoordinateCalculator.placeLabel(allcoords, innerMatrix);
        intermediate = CoordinateCalculator.placeLabel(intermediate, innerScale);
        intermediate = CoordinateCalculator.placeLabel(intermediate, outerScale);
        intermediate = CoordinateCalculator.placeLabel(intermediate, outerMatrix);     */

        double[] intermediate = CoordinateCalculator.placeLabel(allcoords, outerScale);

        String newX = Double.toString(intermediate[0]);
        String newY;
        if(intermediate.length == 3){
            newX = newX.concat(" ").concat(Double.toString(intermediate[1]));
            newY = Double.toString(intermediate[2]);
        }
        else{
            newY = Double.toString(intermediate[1]);
        }
        
        tspan.removeAttribute("x");
        tspan.setAttribute("x", newX);
        tspan.removeAttribute("y");
        tspan.setAttribute("y", newY);

         label.appendChild(tspan);
    }

    public Document createNewDocument(){
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document output = impl.createDocument(svgNS, "svg", null);
        return output;
    }


    public void outputNewSVG(Document doc, String filename){

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());

        //       StreamResult result = new StreamResult(new File("/home/dwelter/testFile.svg"));
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String xmlString = result.getWriter().toString();

        try {
           BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(xmlString);
    //        System.out.println(xmlString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        




    }


}


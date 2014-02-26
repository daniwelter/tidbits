package uk.ac.ebi.fgpt.svg.path.addition;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 22/03/12
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class SVGExtractor {

    public SVGExtractor(String inputfile, String outputfile) {
        Document output = extractSVG(inputfile);

        outputNewSVG(output, outputfile);
    }

    public Document extractSVG(String filename){
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        FileInputStream svgstream = null;
        Document output = null;
        try {
            svgstream = new FileInputStream(filename);
            output = f.createDocument(" file:///home/dwelter/testFile.svg", svgstream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (output != null) {
            Element root = output.getDocumentElement();
            Element g = (Element)root.getElementsByTagName("g").item(0);

            PathProcessor processor = new PathProcessor(g);
            Element newG = processor.getNewG();

            root.removeChild(g);
            root.appendChild(newG);
        }

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

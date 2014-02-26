package uk.ac.ebi.fgpt.RubbishTester;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class StupidStuffTest {

	public static void main(String[] args) {


        String fileName = "/home/dwelter/maskTest.svg";

       String svg = loadTerms(fileName);
 //       System.out.println("Pre conversion " + svg);

        StringReader reader = new StringReader(svg);
 /*       String uri = "file://home/dwelter/maskTest.svg";

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        try {
            Document doc = f.createSVGDocument(uri, reader);
            String converted = getSVG(doc);
            System.out.println("After conversion " + converted);


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }     */

        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                new Float(.8));

        TranscoderInput input = new TranscoderInput(reader);

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("maskout.jpg");
            TranscoderOutput output = new TranscoderOutput(outputStream);

            t.transcode(input, output);

            outputStream.flush();
            outputStream.close();

            System.out.println("Conversion complete");

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public static String loadTerms(String filename){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try{
            reader = new BufferedReader(new FileReader(filename));
            System.out.println("Opened file " + filename);
        }

        catch (FileNotFoundException fnfe)
        {
            System.out.println("Error opening file '" + filename + "'");
        }
        boolean done = false;

        while(!done){
            try{
                String entry = reader.readLine();

                if (entry != null){
                    builder.append(entry);
                }
                else{
                    done = true;
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return builder.toString();
    }

    public static String getSVG(Document doc){

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String xmlString = result.getWriter().toString();

        return xmlString;
    }


}

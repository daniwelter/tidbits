package uk.ac.ebi.fgpt.ENImporter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class FileImporter
{
    public static void main( String[] args )
    {

        File xmlFile = new File("/home/dwelter/Documents/notGWAS.xml");

        FileImporter importer = new FileImporter(xmlFile);
    }

    private ArrayList<Record> allRecords;


    public FileImporter(File xmlFile){

        allRecords = new ArrayList<Record>();
        getRecords(xmlFile);

        try {
            DBLoader loader = new DBLoader();
            loader.uploadRecords(allRecords);
            loader.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

     public void getRecords(File xmlFile){
         System.out.println("Loading file");
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            NodeList allRecs = doc.getElementsByTagName("record");

            System.out.println("There are " + allRecs.getLength() + " records in this file");

            for(int i = 0; i < allRecs.getLength(); i++){

                Element record = (Element) allRecs.item(i);

                String pmid;

                if(record.getElementsByTagName("accession-num").item(0) != null){
                    pmid = record.getElementsByTagName("accession-num").item(0).getTextContent();
                }
                else{
                    pmid = "";
                    System.out.println("Record without pmid in row " + i+1);
                }


                NodeList authors = record.getElementsByTagName("author");

                String author = null;

                if(authors.item(0) != null){

                    author = authors.item(0).getTextContent();
                    author = author.replace(",", "");
                    author = author.replace(".", "");
                }
                else{
                     System.out.println("Record wihtout authors in row " + i+1);
                    author = "";
                }





         //       Element titleEntry = (Element) record.getElementsByTagName("titles").item(0);
                String title;

                if(record.getElementsByTagName("title").item(0) != null){
                    title = record.getElementsByTagName("title").item(0).getTextContent();
                }
                else{
                    title = "";
                    System.out.println("Record without a title in row" + i+1);
                }

     //           Element pubEntry = (Element) record.getElementsByTagName("alt-periodical").item(0);
                String publication;

                if(record.getElementsByTagName("full-title").item(0) != null){
                    publication = record.getElementsByTagName("full-title").item(0).getTextContent();
                }
                else{
                    publication = "";
                    System.out.println("Record without a publication in row " + i+1);
                }


                String date;

                if(record.getElementsByTagName("edition").item(0) != null){
                    date = record.getElementsByTagName("edition").item(0).getTextContent();
                }
                else {
                    date = "";
                    System.out.println("Record wihtout a date in row " + i+1);
                }


//                Date studyDate = Date.valueOf(date);

 //               Element urls = (Element) record.getElementsByTagName("urls").item(0);
  //              Element link = (Element) urls.getElementsByTagName("related-urls").item(0);


                Record latest = new Record(pmid, author, date, publication, title);

                allRecords.add(latest);

            }
            System.out.println("All records successfully loaded");
        }

        catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void saveRecords(){
        System.out.println("Saving records to file");

        String filename = "/home/dwelter/Documents/notGWASOutput.txt";
        try {
            FileWriter writer = new FileWriter(filename);

            for(Record rec : allRecords){
                StringBuilder bldr = new StringBuilder();
                bldr.append(rec.getPmid() + "\t" );
                bldr.append(rec.getAuthor() + "\t");

                if(rec.getStudydate() != null){
                    bldr.append(rec.getStudydate().toString() + "\t");
                }
                else{
                    bldr.append("\t");
                }

                bldr.append(rec.getPublication() + "\t");
                bldr.append(rec.getTitle() + "\n");
                writer.write(bldr.toString());
            }

            writer.close();
            System.out.println("File writer finished");
        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}

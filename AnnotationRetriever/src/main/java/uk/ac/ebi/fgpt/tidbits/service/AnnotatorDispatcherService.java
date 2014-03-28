package uk.ac.ebi.fgpt.tidbits.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.ebi.fgpt.tidbits.exception.DispatcherException;
import javax.json.Json;
import javax.json.stream.JsonParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

/**
 * Created by dwelter on 27/03/14.
 */
public class AnnotatorDispatcherService {
    private HttpContext httpContext;
    private HttpClient httpClient;

    private String annotatorString;



    public AnnotatorDispatcherService(){
        this.httpContext = new BasicHttpContext();
        this.httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
        this.httpClient = new DefaultHttpClient();

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResource("annotator.properties").openStream());
            this.annotatorString = properties.getProperty("bioportal.root")
                    .concat(properties.getProperty("bioportal.annotator"))
                    .concat(properties.getProperty("bioportal.apikey"));
         }
        catch (IOException e) {
            throw new RuntimeException(
                    "Unable to create dispatcher service: failed to read Annotator.properties resource", e);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Unable to create dispatcher service: you must provide a integer query interval " +
                            "in minutes (Annotator.query.interval.mins)", e);
        }
    }

    public Map<String, String> dispatchAnnotatorQuery(Collection<String> cellLines)  throws DispatcherException {


        Map<String, String> results = new HashMap<String, String>();

        try {
            // convert set of supplied cell names to comma separated list
            String idList = "";
            Iterator<String> IdIterator = cellLines.iterator();
            while (IdIterator.hasNext()) {
                idList += IdIterator.next();
                if (IdIterator.hasNext()) {
                    idList += ",";
                }
            }

            Document response = doAnnotatorQuery(URI.create(annotatorString.replace("{inputText}", idList)));

            System.out.println("Annotator query is " + annotatorString.replace("{inputText}", idList));

            NodeList docSumNodes = response.getElementsByTagName("DocumentSummary");

            //NodeList docSumNodes = response.getFirstChild().getChildNodes();

            for (int i = 0; i < docSumNodes.getLength(); i++) {
                Node docSumNode = docSumNodes.item(i);



       
            }

        }
        catch (IOException e) {
            throw new DispatcherException("Communication problem with Bioportal", e);

        }
//      catch (ParseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        return results;
     
    }


    private Document doAnnotatorQuery(URI queryUri) throws IOException {
        HttpGet httpGet = new HttpGet(queryUri);
        HttpResponse response = httpClient.execute(httpGet, httpContext);
        HttpEntity entity = response.getEntity();
        InputStream entityIn = entity.getContent();
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {


                JsonParser parser = Json.createParser(new StringReader(jsonData));
                while (parser.hasNext()) {
                    JsonParser.Event event = parser.next();
                    switch(event) {
                        case START_ARRAY:
                        case END_ARRAY:
                        case START_OBJECT:
                        case END_OBJECT:
                        case VALUE_FALSE:
                        case VALUE_NULL:
                        case VALUE_TRUE:
                            System.out.println(event.toString());
                            break;
                        case KEY_NAME:
                            System.out.print(event.toString() + " " +
                                    parser.getString() + " - ");
                            break;
                        case VALUE_STRING:
                        case VALUE_NUMBER:
                            System.out.println(event.toString() + " " +
                                    parser.getString());
                            break;
                    }
                }
//                try {
//                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//                    return db.parse(entityIn);
//                }
//                catch (SAXException e) {
//                    throw new IOException("Could not parse response from Annotator due to an exception reading content",
//                            e);
//                }
//                catch (ParserConfigurationException e) {
//                    throw new IOException("Could not parse response from Annotator due to an exception reading content",
//                            e);
//                }
            }
            else {
                throw new IOException(
                        "Could not obtain results from '" + queryUri + "' due to an unknown communication problem");
            }
        }
        finally {
            entityIn.close();
        }
    }

}

package uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.service;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.protocol.ClientContext;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HttpContext;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.exception.DispatcherException;
import uk.ac.ebi.fgpt.tidbits.AnnotationRetriever.model.BPAnnotation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by dwelter on 27/03/14.
 */
public class AnnotatorDispatcherService {


    private String annotatorString;

    private List<BPAnnotation> results;
    static private String API_KEY;
    static final ObjectMapper mapper = new ObjectMapper();

    private Logger log = LoggerFactory.getLogger(getClass());
    private String outFile;


    public AnnotatorDispatcherService(){
//        this.httpContext = new BasicHttpContext();
//        this.httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
//        this.httpClient = new DefaultHttpClient();

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResource("annotator.properties").openStream());
            this.annotatorString = properties.getProperty("bioportal.root")
                    .concat(properties.getProperty("bioportal.annotator"))        ;
              //      .concat(properties.getProperty("bioportal.apikey"));

            API_KEY = properties.getProperty("bioportal.apikey");
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

    public List<BPAnnotation> dispatchAnnotatorQuery(Collection<String> cellLines)  throws DispatcherException {


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

            idList = URLEncoder.encode(idList, "ISO-8859-1");

            idList = "text=".concat(idList);

     //       String query = annotatorString.replace("{inputText}", idList);

            System.out.println("Annotator query is " + annotatorString.concat(idList));


            doAnnotatorQuery(idList);



        }
        catch (IOException e) {
            throw new DispatcherException("Communication problem with Bioportal", e);

        }
//      catch (ParseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        return results;
     
    }


    private void doAnnotatorQuery(String queryUri) throws IOException {
//   //     HttpGet httpGet = new HttpGet(queryUri);
//        HttpPost httpPost = new HttpPost(queryUri);
////        HttpResponse response = httpClient.execute(httpGet, httpContext);
//        HttpResponse response = httpClient.execute(httpPost, httpContext);
//        HttpEntity entity = response.getEntity();
//        InputStream entityIn = entity.getContent();
  //      try {
      //      if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                JsonNode annotations = jsonToNode(post(annotatorString, queryUri));
                printAnnotations(annotations, outFile);


        //        results = mapper.readValue(entityIn, new TypeReference<List<BPAnnotation>>() {});

//            }
//            else {
//                throw new IOException(
//                        "Could not obtain results from '" + queryUri + "' due to an unknown communication problem");
//            }
//        }
//        finally {
//  //          entityIn.close();
//        }
    }

    private static void printAnnotations(JsonNode annotations, String fileName) {

        try {
            File f = new File(fileName);

            Writer out= null;

            out = new BufferedWriter(new FileWriter(f));

            out.write("ID \t prefLabel \t ontology \t from \t to \t matchType \t text \n");

            int length = annotations.size();
            System.out.println(length + " nodes to process in total");
            int done = 1;
            for (JsonNode annotation : annotations) {
                if(done%20 == 0){
                    int left = length-done;
                    System.out.println(left + " nodes left to process");
                }
                // Get the details for the class that was found in the annotation and print
    //            System.out.println("Annotation: " + annotation.toString());
                String response = get(annotation.get("annotatedClass").get("links").get("self").toString());
    //            System.out.println("Response: " + response);

                if(response != null){
                    JsonNode classDetails = jsonToNode(response);

                    JsonNode annot = annotation.get("annotations");




                    for(JsonNode data: annot){
                        out.write(classDetails.get("@id").toString() + "\t"
                                + classDetails.get("prefLabel").toString() + "\t"
                                + classDetails.get("links").get("ontology").toString() + "\t");
                        out.write(data.get("from").toString() + "\t"
                                        + data.get("to").toString() + "\t"
                                        + data.get("matchType").toString() + "\t"
                                        + data.get("text").toString() + "\n");
                    }

//                    out.write("\n");
    //                                    + annotation.get("annotations").get("from").toString() + "\t"
    //                                    + annotation.get("annotations").get("to").toString() + "\t"
    //                                    + annotation.get("annotations").get("matchType").toString() + "\t"
    //                                    + annotation.get("annotations").get("text").toString());

    //                System.out.println("\tid: " + classDetails.get("@id").toString());
    //                System.out.println("\tprefLabel: " + classDetails.get("prefLabel").toString());
    //                System.out.println("\tontology: " + classDetails.get("links").get("ontology").toString());
    //                System.out.println("\n");

//                    JsonNode hierarchy = annotation.get("hierarchy");
//                    // If we have hierarchy annotations, print the related class information as well
//                    if (hierarchy.isArray() && hierarchy.getElements().hasNext()) {
//                        System.out.println("\tHierarchy annotations");
//                        for (JsonNode hierarchyAnnotation : hierarchy) {
//                            String resp = get(hierarchyAnnotation.get("annotatedClass").get("links").get("self").toString());
//                            if(resp != null){
//                                classDetails = jsonToNode(resp);
//                                System.out.println("\t\t\tid: " + classDetails.get("@id").toString());
//                                System.out.println("\t\t\tprefLabel: " + classDetails.get("prefLabel").toString());
//                                System.out.println("\t\t\tontology: " + classDetails.get("links").get("ontology").toString());
//                            }
//                        }
//                    }
                    done++;
                }
                else{
                    out.write("Broken API call for " + annotation.get("annotatedClass").get("links").get("self").toString() + "\n");
                    done++;
                }


            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode jsonToNode(String json) {
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static String get(String urlToGet) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        urlToGet=urlToGet.replace("\"","");
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
            conn.setRequestProperty("Accept", "application/json");

            if(conn.getResponseCode() == 200){
                rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            }
            else{
                result = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String post(String urlToGet, String urlParameters) {
        URL url;
        HttpURLConnection conn;

        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            conn.disconnect();

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public String getOutFile() {
        return outFile;
    }
}

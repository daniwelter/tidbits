package uk.ac.ebi.fgpt.ENImporter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 14/05/13
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class Record {

    private String pmid, author, publication, title, date;

    private Date studydate;


    public Record(String pmid, String author, String studydate, String publication, String title){

        this.pmid = pmid.trim();
        this.author = author.trim();
        this.date = studydate.trim();
        this.publication = publication.trim();
        this.title = title.trim();

        if(!date.equals("")){
            setStudydate();
        }
        else{
            this.studydate = null;
        }

    }

    public void setStudydate(){
        date = date.replace("/", "-");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date studyDate = null;
        try {
            studyDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.studydate = new java.sql.Date(studyDate.getTime());

    }

    public String getPmid() {
        return pmid;
    }

    public String getAuthor() {
        if(author.length() > 96){
            author = author.substring(0, 95);
            System.out.println("Author for " + pmid + " is " + author);
        }
        return author;
    }

    public String getPublication() {
        if(publication.length() > 64){
            publication = publication.substring(0, 63);
            System.out.println("Publication for " + pmid + " is " + publication);
        }
        return publication;
    }

    public String getTitle() {
        if(title.length() > 255){
            title = title.substring(0, 254);
            System.out.println("Title for " + pmid + " is " + title);
        }
        return title;
    }

    public Date getStudydate() {
        return studydate;
    }
}

package uk.ac.ebi.fgpt.tidbits;


import org.springframework.context.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.fgpt.tidbits.service.AnnotationRetriever;

public class RetrieverDriver {


    public static void main(String args[]){
        RetrieverDriver driver = new RetrieverDriver();
        driver.annotate();

    }

    private AnnotationRetriever retriever;

    public RetrieverDriver(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("annotationretriever.xml");
        retriever = ctx.getBean("searcher",AnnotationRetriever.class);

    }

    public void annotate(){
        retriever.dispatchAnnotator();
        System.out.println("All annotations retrieved");
    }

}

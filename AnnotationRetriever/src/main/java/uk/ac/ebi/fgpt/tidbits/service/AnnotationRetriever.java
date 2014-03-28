package uk.ac.ebi.fgpt.tidbits.service;

import uk.ac.ebi.fgpt.tidbits.exception.DispatcherException;

import java.util.Collection;

/**
 * Created by dwelter on 27/03/14.
 */
public class AnnotationRetriever {
    private AnnotatorDispatcherService dispatcherService;
    private DBLoader dbLoader;

    public void setDispatcherService(AnnotatorDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    public AnnotatorDispatcherService getDispatcherService() {
        return dispatcherService;
    }

    public void setDbLoader(DBLoader dbLoader) {
        this.dbLoader = dbLoader;
    }

    public DBLoader getDbLoader() {
        return dbLoader;
    }


    public void dispatchAnnotator(){
        Collection<String> cellData = getDbLoader().retrieveAllEntries();

        try {
            getDispatcherService().dispatchAnnotatorQuery(cellData);
        } catch (DispatcherException e) {
            e.printStackTrace();
        }
    }
}

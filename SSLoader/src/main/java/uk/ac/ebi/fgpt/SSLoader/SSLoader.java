package uk.ac.ebi.fgpt.SSLoader;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class SSLoader
{
    public static void main( String[] args )
    {
        String name = "/home/dwelter/tidbits/loaderTest.xlsx";

        try{

            String filename = name;
            XSSFSheet sheet = null;

            OPCPackage pkg = OPCPackage.open(filename);
            XSSFWorkbook current = new XSSFWorkbook(pkg);
            System.out.println("Acquiring 0-index sheet...");

            sheet = current.getSheetAt(0);
            System.out.println("Got sheet 0 OK!");

            SheetProcessor processor = new SheetProcessor(sheet);

            ArrayList<SNPentry> SNPlist = processor.getSNPlist();

            for(SNPentry snp : SNPlist){
                System.out.print(snp.getGene() + "\t");
                System.out.print(snp.getAllele() + "\t");
                System.out.print(snp.getSNP() + "\t");
                System.out.print(snp.getRiskFreq() + "\t");
                System.out.print(snp.getpnum() + "\t");
                System.out.print(snp.getptxt() + "\t");
                System.out.print(snp.getORnum() + "\t");
                System.out.print(snp.getORrecip() + "\t");
                System.out.print(snp.getRange() + "\t");
                System.out.print(snp.getORstderr() + "\n");
            }

            pkg.close();

            System.out.println("Upload successful");

        }
        catch (Exception e) {
            System.out.println("Encountered a " + e.getClass().getSimpleName() + " whilst trying to upload file '" + name + "'" +
                    " (" + new File(name).getAbsolutePath() + ")" + e);
           e.printStackTrace();
 //           throw e;
        }
//        finally{
//            File f = new File(name);
//            if(f.delete()){
//                System.out.println("File deleted");
//            }
//            else{
//                System.out.println("Failure to delete file");
//            }
//
//        }
    }
}

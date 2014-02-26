package uk.ac.ebi.fgpt.RubbishTester;

/**
 * Created with IntelliJ IDEA.
 * User: dwelter
 * Date: 10/09/12
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SaveAsJPEG {

    public static void main(String[] args) throws Exception {

        String fileName = "/home/dwelter/colours.svg";

        // Create a JPEG transcoder
        JPEGTranscoder t = new JPEGTranscoder();

        // Set the transcoding hints.
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                new Float(.8));

        // Create the transcoder input.
        String svgURI = new File(fileName).toURL().toString();
        TranscoderInput input = new TranscoderInput(svgURI);

        // Create the transcoder output.
        OutputStream ostream = new FileOutputStream("out.jpg");
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Save the image.
        t.transcode(input, output);

        // Flush and close the stream.
        ostream.flush();
        ostream.close();
        System.exit(0);
    }
}
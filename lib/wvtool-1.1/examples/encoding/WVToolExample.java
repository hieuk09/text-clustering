import java.io.FileWriter;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.stemmer.LovinsStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.ToLowerCaseConverter;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * An example program on how to use the word vector tool.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class WVToolExample {

    public static void main(String[] args) throws Exception {

        // EXAMPLE HOW TO CALL THE PROGRAM FROM JAVA

        // Initialize the WVTool
        WVTool wvt = new WVTool(false);

        // Initialize the configuration
        WVTConfiguration config = new WVTConfiguration();

        final WVTStemmer dummyStemmer = new DummyStemmer();
        final WVTStemmer porterStemmer = new PorterStemmerWrapper();

        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(new ToLowerCaseConverter()));

        // Initialize the input list with two classes
        WVTFileInputList list = new WVTFileInputList(0);

        // Add entries
        list.addEntry(new WVTDocumentInfo("../data/german_utf8", "txt", "utf-8", "german"));
        list.addEntry(new WVTDocumentInfo("../data/german_iso", "txt", "iso-8859-1", "german"));

        // Generate the word list

        WVTWordList wordList = wvt.createWordList(list, config);

        // Prune the word list

        //wordList.pruneByFrequency(2, 5);

        // Alternativ I: read an already created word list from a file
        // WVTWordList wordList2 =
        // new WVTWordList(new FileReader("/home/wurst/tmp/wordlisttest.txt"));

        // Alternative II: Use predifined dimensions
        // List dimensions = new Vector();
        // dimensions.add("atheist");
        // dimensions.add("christian");
        // wordList =
        // wvt.createWordList(list, config, dimensions, false);

        // Store the word list in a file
        wordList.storePlain(new FileWriter("wordlist.txt"));

        // Create the word vectors

        // Set up an output filter (write sparse vectors to a file)
        FileWriter outFile = new FileWriter("wv.txt");
        WordVectorWriter wvw = new WordVectorWriter(outFile, true);

        config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));

        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));

        // Create the vectors
        wvt.createVectors(list, config, wordList);

        // Alternatively: create word list and vectors together
        // wvt.createVectors(list, config);

        // Close the output file
        wvw.close();
        outFile.close();


    }

}

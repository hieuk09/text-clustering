import java.io.FileWriter;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.stemmer.LovinsStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WordList2AMLFile;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class WVToolYaleOutput {

    public static void main(String[] args) throws Exception {

        // EXAMPLE HOW TO CALL THE PROGRAM FROM JAVA

        // Initialize the WVTool
        WVTool wvt = new WVTool(false);

        // Initialize the configuration
        WVTConfiguration config = new WVTConfiguration();

        final WVTStemmer dummyStemmer = new DummyStemmer();
        final WVTStemmer porterStemmer = new PorterStemmerWrapper();

        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationRule() {
            public Object getMatchingComponent(WVTDocumentInfo d) {

                if (d.getContentLanguage().equals("english"))
                    return porterStemmer;
                else
                    return dummyStemmer;
            }
        });

        WVTStemmer stemmer = new LovinsStemmerWrapper();

        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));

        // Initialize the input list with two classes
        WVTFileInputList list = new WVTFileInputList(2);

        // Add entries
        list.addEntry(new WVTDocumentInfo("../data/alt.atheism", "txt", "", "german", 0));
        list.addEntry(new WVTDocumentInfo("../data/soc.religion.christian", "txt", "", "english", 1));

        // Generate the word list

        WVTWordList wordList = wvt.createWordList(list, config);

        // Prune the word list
        wordList.pruneByFrequency(2, 5);

        // Store the aml file
        WordList2AMLFile.storeWordList(wordList, new FileWriter("test_wv.aml"), true, "wv.dat");
        // Create the word vectors

        // Set up an output filter (write sparse vectors to a file)
        FileWriter outFile = new FileWriter("wv.dat");
        WordVectorWriter wvw = new WordVectorWriter(outFile, true, true, true, 1);

        config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));

        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));

        // Create the vectors
        wvt.createVectors(list, config, wordList);

        // Close the output file
        wvw.close();
        outFile.close();

       
    }
    
    
}

import java.io.FileReader;
import java.io.FileWriter;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.DictionaryStemmer;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.stemmer.WordNetHypernymStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class WVToolDictionaryExample {

    public static void main(String[] args) throws Exception {

        // Initialize the WVTool
        WVTool wvt = new WVTool(false);

        // Initialize the configuration
        WVTConfiguration config = new WVTConfiguration();

        WVTStemmer stemmer = new DictionaryStemmer(new FileReader("../data/sample_dictionary.txt"));

        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));

        // Initialize the input list with two classes
        WVTFileInputList list = new WVTFileInputList(2);

        // Add entries
        list.addEntry(new WVTDocumentInfo("../data/alt.atheism", "txt", "", "german", 0));
        list.addEntry(new WVTDocumentInfo("../data/soc.religion.christian", "txt", "", "english", 1));

        // Generate the word list

        WVTWordList wordList = wvt.createWordList(list, config);

        // Prune the word list
        //wordList.pruneByFrequency(1, 5);

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

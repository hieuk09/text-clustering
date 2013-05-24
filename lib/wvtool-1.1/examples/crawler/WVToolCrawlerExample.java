import java.io.FileWriter;
import java.net.URL;

import websphinx.Link;
import websphinx.Page;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.crawler.CrawledInputList;
import edu.udo.cs.wvtool.crawler.WVToolCrawler;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class WVToolCrawlerExample {

    public static void main(String[] args) throws Exception {

        // Initialize the WVTool
        WVTool wvt = new WVTool(true);

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

        WVToolCrawler test = new WVToolCrawler("", "", "") {

            protected boolean vectorizePage(Page page) {

                String url = page.getURL().toExternalForm();

                return (url.indexOf("PERSONAL")>-1) && (url.indexOf("html")>-1) && (!(url.indexOf("index") > -1));
            }

            public boolean shouldVisit(Link arg0) {

                return arg0.getPageURL().toExternalForm().indexOf("PERSONAL") > -1;

            }
        };

        test.addRoot(new Link(new URL("http://www-ai.cs.uni-dortmund.de/PERSONAL/index.html")));
        test.setMaxDepth(2);

        // Initialize the input list with two classes
        WVTInputList list = new CrawledInputList(test);

        // Generate the word list

        WVTWordList wordList = wvt.createWordList(list, config);

        // Prune the word list
        wordList.pruneByFrequency(2, 5);

        wordList.storePlain(new FileWriter("wordlist.txt"));

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

        // Just for demonstration: Create a vector from a String
        WVTWordVector q = wvt.createVector("cmu harvard net", wordList);

    }

}
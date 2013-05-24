  /*
  WVTool - Word Vector Tool
  Copyright (C) 2001-2007

	    Michael Wurst       

  web:   http://wvtool.sourceforge.net

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as 
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version. 

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA.
*/
package edu.udo.cs.wvtool.main;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.generic.charmapper.WVTCharConverter;
import edu.udo.cs.wvtool.generic.inputfilter.WVTInputFilter;
import edu.udo.cs.wvtool.generic.loader.WVTDocumentLoader;
import edu.udo.cs.wvtool.generic.output.WVTOutputFilter;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.tokenizer.WVTTokenizer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.generic.vectorcreation.WVTVectorCreator;
import edu.udo.cs.wvtool.generic.wordfilter.WVTWordFilter;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * Main class of the word vector tool. It provides all the functionality and can be called directly via java.
 * 
 * @author Michael Wurst
 * @version $Id: WVTool.java,v 1.6 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */
public class WVTool {

    /** lower boundary for automatic pruning */
    private static final int DEFAULT_PRUNE_MIN = 4;

    /** upper boundary for automatic pruning */
    private static final int DEFAULT_PRUNE_MAX = 2000;

    /** should errors be skiped */
    private boolean skipErrors = true;

    /**
     * Create a new WVTool instance.
     * 
     * @param skipErrors should errors be skip (and only be written to the error log) or should an Exception be thrown
     */
    public WVTool(boolean skipErrors) {

        this.skipErrors = skipErrors;

    }

    public InputStream getInputStream(WVTDocumentInfo d, WVTConfiguration config) throws WVToolException {

        WVTDocumentLoader loader = null;
        loader = (WVTDocumentLoader) config.getComponentForStep(WVTConfiguration.STEP_LOADER, d);

        return loader.loadDocument(d);

    }

    public Reader getReader(WVTDocumentInfo d, WVTConfiguration config) throws WVToolException {

        WVTDocumentLoader loader = null;
        WVTInputFilter infilter = null;

        loader = (WVTDocumentLoader) config.getComponentForStep(WVTConfiguration.STEP_LOADER, d);
        infilter = (WVTInputFilter) config.getComponentForStep(WVTConfiguration.STEP_INPUT_FILTER, d);

        return infilter.convertToPlainText(loader.loadDocument(d), d);

    }

    /**
     * Create a word list from scrat based on the given texts.
     * 
     * @param input the input list from which word list is created
     * @param config the underlying configuration
     * @return a WVTWordList object
     * @throws Exception
     */
    public WVTWordList createWordList(WVTInputList input, WVTConfiguration config) throws WVToolException {

        return createWordList(input, config, new LinkedList(), true);

    }

    /**
     * Create a word list based on an existing word list.
     * 
     * @param input the input list from which word list is created
     * @param config the underlying configuration
     * @param initialWords initial list of words to use
     * @param addWords should words, appearing in texts but not in the initial list be added to the list
     * @return a WVTWordList object
     * @throws Exception
     */
    public WVTWordList createWordList(WVTInputList input, WVTConfiguration config, List initialWords, boolean addWords) throws WVToolException {

        // Initialize the word list
        WVTWordList wordList = new WVTWordList(initialWords, input.getNumClasses());
        wordList.setAppendWords(addWords);
        wordList.setUpdateOnlyCurrent(false);

        // Initialize pointers to components for the individual steps
        WVTDocumentLoader loader = null;
        WVTInputFilter infilter = null;
        WVTCharConverter charConverter = null;
        WVTTokenizer tokenizer = null;
        WVTWordFilter wordFilter = null;
        WVTStemmer stemmer = null;

        // Obtain an expanded list of all documents to consider
        Iterator inList = input.getEntries();

        // Get through the list
        while (inList.hasNext()) {

            WVTDocumentInfo d = (WVTDocumentInfo) inList.next();

            try {

                // Intialize all required components for this document

                loader = (WVTDocumentLoader) config.getComponentForStep(WVTConfiguration.STEP_LOADER, d);
                infilter = (WVTInputFilter) config.getComponentForStep(WVTConfiguration.STEP_INPUT_FILTER, d);
                charConverter = (WVTCharConverter) config.getComponentForStep(WVTConfiguration.STEP_CHAR_MAPPER, d);
                tokenizer = (WVTTokenizer) config.getComponentForStep(WVTConfiguration.STEP_TOKENIZER, d);
                wordFilter = (WVTWordFilter) config.getComponentForStep(WVTConfiguration.STEP_WORDFILTER, d);
                stemmer = (WVTStemmer) config.getComponentForStep(WVTConfiguration.STEP_STEMMER, d);

                // Process the document

                TokenEnumeration tokens = stemmer.stem(wordFilter.filter(tokenizer.tokenize(charConverter.convertChars(infilter.convertToPlainText(loader.loadDocument(d), d), d), d), d), d);

                while (tokens.hasMoreTokens()) {
                    wordList.addWordOccurance(tokens.nextToken());
                }

                wordList.closeDocument(d);
                loader.close(d);

            } catch (WVToolException e) {

                WVToolLogger.getGlobalLogger().logException("Problems processing document " + d.getSourceName(), e);

                // close the input stream for this document

                loader.close(d);

                // If errors should not be skip throw an exception
                if (!skipErrors) {
                    throw new WVToolException("Problems processing document " + d.getSourceName(), e);

                }
                // otherwise do nothing and proceed with the next document

            }

        }

        return wordList;
    }

    /**
     * Create a word list and after this word vectors, both from the same input list.
     * 
     * @param input the input list
     * @param config the configuration
     * @param pruneMin the minimal number of occurences of a word to be considered
     * @param pruneMax the maximum number of occurences of a word to be considered
     * 
     */
    public void createVectors(WVTInputList input, WVTConfiguration config, int pruneMin, int pruneMax) throws WVToolException {

        // Currently somewhat unefficient: Simply reads all documents twice,
        // will be enhanced in future versions.

        // Generate the word list
        WVTWordList wordList = createWordList(input, config);

        // Prune the word list
        wordList.pruneByFrequency(DEFAULT_PRUNE_MIN, DEFAULT_PRUNE_MAX);

        // Create the word vectors
        createVectors(input, config, wordList);

    }

    /**
     * Create a word list and after this word vectors, both from the same input list.
     * 
     * @param input the input list
     * @param config the configuration
     * @deprecated Please use the method createVectors(WVTInputList input, WVTConfiguration config, int pruneMin, int pruneMax)
     */
    public void createVectors(WVTInputList input, WVTConfiguration config) throws WVToolException {

        createVectors(input, config, DEFAULT_PRUNE_MIN, DEFAULT_PRUNE_MAX);

    }

    /**
     * Create word vectors from an input list.
     * 
     * @param input the input list
     * @param config the configuration
     * @param wordList a word list (possibly containing document and class frequencies).
     * @throws Exception
     */
    public void createVectors(WVTInputList input, WVTConfiguration config, WVTWordList wordList) throws WVToolException {

        // Set up the word list properly

        wordList.setAppendWords(false);
        wordList.setUpdateOnlyCurrent(true);

        // Initialize pointers to components for the individual steps
        WVTDocumentLoader loader = null;
        WVTInputFilter infilter = null;
        WVTCharConverter charConverter = null;
        WVTTokenizer tokenizer = null;
        WVTWordFilter wordFilter = null;
        WVTStemmer stemmer = null;
        WVTVectorCreator vectorCreator = null;
        WVTOutputFilter outputFilter = null;

        // Obtain an expanded list of all documents to consider
        Iterator inList = input.getEntries();

        // Get through the list
        while (inList.hasNext()) {

            WVTDocumentInfo d = (WVTDocumentInfo) inList.next();

            try {

                // Intialize all required components for this document

                loader = (WVTDocumentLoader) config.getComponentForStep(WVTConfiguration.STEP_LOADER, d);
                infilter = (WVTInputFilter) config.getComponentForStep(WVTConfiguration.STEP_INPUT_FILTER, d);
                charConverter = (WVTCharConverter) config.getComponentForStep(WVTConfiguration.STEP_CHAR_MAPPER, d);
                tokenizer = (WVTTokenizer) config.getComponentForStep(WVTConfiguration.STEP_TOKENIZER, d);
                wordFilter = (WVTWordFilter) config.getComponentForStep(WVTConfiguration.STEP_WORDFILTER, d);
                stemmer = (WVTStemmer) config.getComponentForStep(WVTConfiguration.STEP_STEMMER, d);

                vectorCreator = (WVTVectorCreator) config.getComponentForStep(WVTConfiguration.STEP_VECTOR_CREATION, d);

                outputFilter = (WVTOutputFilter) config.getComponentForStep(WVTConfiguration.STEP_OUTPUT, d);

                // Process the document

                TokenEnumeration tokens = stemmer.stem(wordFilter.filter(tokenizer.tokenize(charConverter.convertChars(infilter.convertToPlainText(loader.loadDocument(d), d), d), d), d), d);

                while (tokens.hasMoreTokens()) {
                    wordList.addWordOccurance(tokens.nextToken());
                }

                outputFilter.write(vectorCreator.createVector(wordList.getFrequenciesForCurrentDocument(), wordList.getTermCountForCurrentDocument(), wordList, d));

                wordList.closeDocument(d);
                loader.close(d);

            } catch (WVToolException e) {

                // If an error occurs add it to the error log
                WVToolLogger.getGlobalLogger().logException("Problems processing document " + d.getSourceName(), e);

                // close the input stream for this document
                loader.close(d);

                // If errors should not be skip throw an exception
                if (!skipErrors)
                    throw new WVToolException("Problems processing document " + d.getSourceName(), e);

                // otherwise do nothing and proceed with the next document

            }

        }

    }

    /**
     * Create a single word vector.
     * 
     * @param text the underlying text
     * @param d information about the text
     * @param config the configuration to use (though it will be only partly used)
     * @param wordList the word list to use
     * @return WVTWordVector
     */
    public WVTWordVector createVector(String text, WVTDocumentInfo d, WVTConfiguration config, WVTWordList wordList) throws WVToolException {

        // Set up the word list properly

        wordList.setAppendWords(false);
        wordList.setUpdateOnlyCurrent(true);

        // Initialize pointers to components for the individual steps

        WVTCharConverter charConverter = null;
        WVTTokenizer tokenizer = null;
        WVTWordFilter wordFilter = null;
        WVTStemmer stemmer = null;
        WVTVectorCreator vectorCreator = null;

        WVTWordVector result = null;

        try {

            // Intialize all required components for this document

            charConverter = (WVTCharConverter) config.getComponentForStep(WVTConfiguration.STEP_CHAR_MAPPER, d);
            tokenizer = (WVTTokenizer) config.getComponentForStep(WVTConfiguration.STEP_TOKENIZER, d);
            wordFilter = (WVTWordFilter) config.getComponentForStep(WVTConfiguration.STEP_WORDFILTER, d);
            stemmer = (WVTStemmer) config.getComponentForStep(WVTConfiguration.STEP_STEMMER, d);

            vectorCreator = (WVTVectorCreator) config.getComponentForStep(WVTConfiguration.STEP_VECTOR_CREATION, d);

            // Process the document

            TokenEnumeration tokens = stemmer.stem(wordFilter.filter(tokenizer.tokenize(charConverter.convertChars(new StringReader(text), d), d), d), d);

            while (tokens.hasMoreTokens()) {
                wordList.addWordOccurance(tokens.nextToken());
            }

            result = vectorCreator.createVector(wordList.getFrequenciesForCurrentDocument(), wordList.getTermCountForCurrentDocument(), wordList, d);

            wordList.closeDocument(d);

        } catch (WVToolException e) {

            WVToolLogger.getGlobalLogger().logException("Problems processing document " + d.getSourceName(), e);

            // If errors should not be skip throw an exception
            if (!skipErrors)
                throw new WVToolException("Problems processing document " + d.getSourceName(), e);
            // otherwise do nothing and proceed with the next document

        }

        return result;
    }

    /**
     * Create an individual word vector from a String using TF/IDF weights and stadard configuration.
     * 
     * @param text the underlying text
     * @param wordList a wordlist (for IDF)
     * @return a WVTWordVector
     * @throws Exception
     */
    public WVTWordVector createVector(String text, WVTWordList wordList) throws WVToolException {

        WVTConfiguration config = new WVTConfiguration();

        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));

        return createVector(text, new WVTDocumentInfo("", "", "", ""), config, wordList);

    }

    /**
     * Process the specified documents using the configured steps and send all encountered words to a listener class. This method can be used to implement specialized applications that merely use the preprocessing steps of the tool instead of using the vectorization functions.
     * 
     * @param input the input list
     * @param config the configuration
     * @param listener a call back class that is invoked on every processed document and word
     * @throws WVToolException
     */
    public void iterateWords(WVTInputList input, WVTConfiguration config, WVToolWordListener listener) throws WVToolException {

        // Initialize pointers to components for the individual steps
        WVTDocumentLoader loader = null;
        WVTInputFilter infilter = null;
        WVTCharConverter charConverter = null;
        WVTTokenizer tokenizer = null;
        WVTWordFilter wordFilter = null;
        WVTStemmer stemmer = null;

        // Obtain an expanded list of all documents to consider
        Iterator inList = input.getEntries();

        // Get through the list
        while (inList.hasNext()) {

            WVTDocumentInfo d = (WVTDocumentInfo) inList.next();
            listener.openNewDocument(d);

            try {

                // Intialize all required components for this document

                loader = (WVTDocumentLoader) config.getComponentForStep(WVTConfiguration.STEP_LOADER, d);
                infilter = (WVTInputFilter) config.getComponentForStep(WVTConfiguration.STEP_INPUT_FILTER, d);
                charConverter = (WVTCharConverter) config.getComponentForStep(WVTConfiguration.STEP_CHAR_MAPPER, d);
                tokenizer = (WVTTokenizer) config.getComponentForStep(WVTConfiguration.STEP_TOKENIZER, d);
                wordFilter = (WVTWordFilter) config.getComponentForStep(WVTConfiguration.STEP_WORDFILTER, d);
                stemmer = (WVTStemmer) config.getComponentForStep(WVTConfiguration.STEP_STEMMER, d);

                // Process the document

                TokenEnumeration tokens = stemmer.stem(wordFilter.filter(tokenizer.tokenize(charConverter.convertChars(infilter.convertToPlainText(loader.loadDocument(d), d), d), d), d), d);

                while (tokens.hasMoreTokens()) {
                    listener.processWord(tokens.nextToken());
                }

                loader.close(d);

            } catch (WVToolException e) {

                // If an error occurs add it to the error log
                WVToolLogger.getGlobalLogger().logException("Problems processing document " + d.getSourceName(), e);

                // close the input stream for this document
                loader.close(d);

                // If errors should not be skip throw an exception
                if (!skipErrors)
                    throw new WVToolException("Problems processing document " + d.getSourceName(), e);

                // otherwise do nothing and proceed with the next document

            }

        }
    }

}

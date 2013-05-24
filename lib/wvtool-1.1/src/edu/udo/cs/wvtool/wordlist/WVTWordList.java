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
package edu.udo.cs.wvtool.wordlist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * This class represents a word list. It is used to store information about individual words, to count words and to calculate the vectors.
 * 
 * @author Michael Wurst
 * @version $Id: WVTWordList.java,v 1.6 2007/05/22 18:07:27 mjwurst Exp $
 * 
 */
public class WVTWordList {

    /** A Hash used to find words efficiently */
    private Map wordMap = null;

    /**
     * A sequential indexing structure, to ensure a fixed order of all words in the list
     */
    private List wordList = null;

    /** the number of possible class values */
    private int numClasses = 0;

    /** indicates, whether missing words should be added to the list */
    private boolean appendWords = true;

    /**
     * indicates, whether the document and class frequencies should be updated as well, or only the frequencies for the current document
     */
    private boolean updateOnlyCurrent = true;

    /** the number of documents processed so far */
    private int numDocuments = 0;

    /** the number of terms processed in the current document so far */
    private int numLocalTerms = 0;

    /**
     * Create a new instance of WVTWordList.
     * 
     * @param numClasses the number of possible class values
     */
    public WVTWordList(int numClasses) {

        wordList = new ArrayList();
        wordMap = new HashMap();

        this.numClasses = numClasses;
    }

    public WVTWordList(List words, int numClasses) {

        this(numClasses);

        for (int i = 0; i < words.size(); i++) {

            String word = (String) words.get(i);

            WVTWord w = null;

            if (numClasses > 0)
                w = new WVTWord(word, numClasses);
            else
                w = new WVTWord(word);

            wordMap.put(word, w);
            wordList.add(w);

        }

    }

    /**
     * Create a new instance of WVTWordList by reading it from a stream.
     * 
     * @param in the stream from which to read the information
     */
    public WVTWordList(Reader in) {

        try {

            wordList = new ArrayList();
            wordMap = new HashMap();

            BufferedReader inb = new BufferedReader(in);

            String buf = inb.readLine();
            StringTokenizer buf2 = new StringTokenizer(buf);

            if (buf2.nextToken().equals("@number_of_documents")) {

                numDocuments = Integer.parseInt(buf2.nextToken());
            }

            buf = inb.readLine();
            buf2 = new StringTokenizer(buf);

            numClasses = 0;

            if (buf2.nextToken().equals("@number_of_classes")) {

                numClasses = Integer.parseInt(buf2.nextToken());
            }

            while ((buf = inb.readLine()) != null) {

                StringTokenizer tokenizer = new StringTokenizer(buf, ",");
                String term = tokenizer.nextToken();
                WVTWord word = new WVTWord(term, numClasses);

                wordList.add(word);
                wordMap.put(term, word);

                if (tokenizer.hasMoreTokens()) {

                    String freqStr = tokenizer.nextToken();
                    word.setDocumentFrequency(Integer.parseInt(freqStr));

                    for (int i = 0; i < numClasses; i++) {

                        if (tokenizer.hasMoreTokens()) {

                            String classFreqStr = tokenizer.nextToken();
                            word.setClassFrequency(i, Integer.parseInt(classFreqStr));

                        }

                    }
                }
            }

            inb.close();

        } catch (IOException e) {
            WVToolLogger.getGlobalLogger().logException("Could not read word list.", e);
        }

    }

    /**
     * Count the occurance of the given word.
     * 
     * @param word the word
     */
    public void addWordOccurance(String word) {

        // Try to find it with the hash
        WVTWord w = (WVTWord) wordMap.get(word);

        // If not found and new words should be added add it
        if ((w == null) && (appendWords)) {
            if (numClasses > 0)
                w = new WVTWord(word, numClasses);
            else
                w = new WVTWord(word);

            wordMap.put(word, w);
            wordList.add(w);
        }

        if (w != null)
            w.addOccurance();

        numLocalTerms++;
    }

    /**
     * Used to reset the calculation for individual documents after the given document has been processed.
     * 
     * @param d information about the document
     */
    public void closeDocument(WVTDocumentInfo d) {

        // Call close document for each word

        if (d.hasClassValue())
            for (int i = 0; i < wordList.size(); i++)
                ((WVTWord) wordList.get(i)).closeDocument(d.getClassValue(), updateOnlyCurrent);
        else
            for (int i = 0; i < wordList.size(); i++)
                ((WVTWord) wordList.get(i)).closeDocument(updateOnlyCurrent);

        // Increase the number of documents processed
        if (!updateOnlyCurrent)
            numDocuments++;

        numLocalTerms = 0;
    }

    /**
     * Get the word frequencies for the document that is currently processed.
     * 
     * @return an array containing the word frequencies
     */
    public int[] getFrequenciesForCurrentDocument() {

        int[] freq = new int[wordList.size()];

        for (int i = 0; i < wordList.size(); i++)
            freq[i] = ((WVTWord) wordList.get(i)).getLocalFrequency();

        return freq;
    }

    public int getTermCountForCurrentDocument() {

        return numLocalTerms;
    }

    /**
     * Get the document frequencies.
     * 
     * @return an array containing the document frequencies
     */
    public int[] getDocumentFrequencies() {

        int[] freq = new int[wordList.size()];

        for (int i = 0; i < wordList.size(); i++)
            freq[i] = ((WVTWord) wordList.get(i)).getDocumentFrequency();

        return freq;
    }

    /**
     * Get the document frequencies of documents having a given class value.
     * 
     * @param classValue the class value
     * @return an array containing the document frequencies for the given class
     */
    public int[] getClassFrequencies(int classValue) {

        int[] freq = new int[wordList.size()];

        for (int i = 0; i < wordList.size(); i++)
            freq[i] = ((WVTWord) wordList.get(i)).getClassFrequency(classValue);

        return freq;

    }

    /**
     * Write the wordlist to a stream.
     * 
     * @param out the stream to which to write the word list
     */
    public void store(Writer out) {

        PrintWriter file = new PrintWriter(new BufferedWriter(out));

        // Write the global properties of the wordlist

        file.println("@number_of_documents " + numDocuments);
        file.println("@number_of_classes " + numClasses);

        // Write the individual documents

        WVTWord currentWord;
        for (int i = 0; i < wordList.size(); i++) {
            currentWord = (WVTWord) wordList.get(i);

            file.print(currentWord.getWord() + "," + currentWord.getDocumentFrequency());

            for (int j = 0; j < numClasses; j++) {
                file.print("," + currentWord.getClassFrequency(j));
            }

            file.println("");

        }

        file.close();
    }

    /**
     * Write the wordlist to a stream without any additional info.
     * 
     * @param out the stream to which to write the word list
     */
    public void storePlain(Writer out) {

        PrintWriter file = new PrintWriter(new BufferedWriter(out));

        // Write the individual documents

        WVTWord currentWord;
        for (int i = 0; i < wordList.size(); i++) {
            currentWord = (WVTWord) wordList.get(i);

            file.println(currentWord.getWord());
        }

        file.close();
    }

    /**
     * Returns the appendWords.
     * 
     * @return boolean
     */
    public boolean isAppendWords() {
        return appendWords;
    }

    /**
     * Returns the updateOnlyCurrent.
     * 
     * @return boolean
     */
    public boolean isUpdateOnlyCurrent() {
        return updateOnlyCurrent;
    }

    /**
     * Sets the appendWords.
     * 
     * @param appendWords The appendWords to set
     */
    public void setAppendWords(boolean appendWords) {
        this.appendWords = appendWords;
    }

    /**
     * Sets the updateOnlyCurrent.
     * 
     * @param updateOnlyCurrent The updateOnlyCurrent to set
     */
    public void setUpdateOnlyCurrent(boolean updateOnlyCurrent) {
        this.updateOnlyCurrent = updateOnlyCurrent;
    }

    /**
     * Returns the numDocuments.
     * 
     * @return int
     */
    public int getNumDocuments() {
        return numDocuments;
    }

    /**
     * Return the number of words in the list.
     * 
     * @return the number of words
     */
    public int getNumWords() {

        return wordList.size();
    }

    /**
     * Prune the word list by document frequencies.
     * 
     * @param min minimal document frequency, all words with less frequency will be deleted
     * @param max maximal document frequency, all words with more frequency will be deleted
     */
    public void pruneByFrequency(int min, int max) {

        Iterator e = wordMap.values().iterator();
        while (e.hasNext()) {
            WVTWord currentWord = (WVTWord) e.next();
            if ((currentWord.getDocumentFrequency() < min) || (currentWord.getDocumentFrequency() > max)) {
                wordList.remove(currentWord);

            }
        }

    }

    /**
     * Returns the document frequency of the word that is on the p-th rank, assuming that each word occupies exactly one rank. Ranks start by 1.
     * 
     * @param p the rank of the word starting with 1 for the first rank
     * @return the document frequency of the word on the p-pth rank.
     */
    public int getFrequencyByRank(int p) {

        // If the word list does not contain enough entries, return 0
        if (getNumWords() < p)
            return 0;

        int[] frequencies = new int[getNumWords()];
        Iterator it = wordList.iterator();
        int counter = 0;
        while (it.hasNext()) {

            WVTWord word = (WVTWord) it.next();
            frequencies[counter] = word.getDocumentFrequency();
            counter++;
        }

        Arrays.sort(frequencies);
        return frequencies[frequencies.length - p];

    }

    /**
     * Returns the WVTWord with the given index.
     * 
     * @param index the index of the word
     */
    public String getWord(int index) {
        return ((WVTWord) wordList.get(index)).getWord();
    }
}

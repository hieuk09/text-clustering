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

/**
 * Class, which represents an individual word and its occurances (class and document frequencies). It is also used as counter to count word occurances in a currently processed document.
 * 
 * @author Michael Wurst
 * @version $Id: WVTWord.java,v 1.3 2007/05/20 18:06:04 mjwurst Exp $
 * 
 */
class WVTWord {

    /** the word */
    private final String word;

    /** counter for the class frequencies */
    private int classCount[];

    /** counter for the document frequencies */
    private int documentCount;

    /** counter for the term frequencies in the currently processed document */
    private int localCount;

    /**
     * Create a new instance of Word.
     * 
     * @param word the word as a String
     * @param numClasses the number of possible class values
     */
    public WVTWord(String word, int numClasses) {

        this.word = word;

        classCount = new int[numClasses];
        for (int i = 0; i < numClasses; i++)
            classCount[i] = 0;

        localCount = 0;
        documentCount = 0;
    }

    /**
     * Create a new instance of Word, not supporting class values.
     * 
     * @param word the word as a String
     */
    public WVTWord(String word) {

        this.word = word;

        classCount = null;
        localCount = 0;
        documentCount = 0;

    }

    /**
     * Add an occurance of the word for the document currently processed.
     */
    public void addOccurance() {

        localCount++;
    }

    /**
     * Close the processing of the current document, no class value is provided for this document.
     * 
     * @param onlyReset should only the local counter be reseted (without changing the other frequencies)
     */
    public void closeDocument(boolean onlyReset) {

        if (localCount > 0) {

            if (!onlyReset)
                documentCount++;

            localCount = 0;

        }
    }

    /**
     * Close the processing of the current document, if a class value is provided.
     * 
     * @param classValue the class value for the document, which has been just processed
     * @param onlyReset should only the local counter be reseted (without changing the other frequencies)
     */
    public void closeDocument(int classValue, boolean onlyReset) {

        // If the current document contained this word at least once, increase
        // the document count
        if ((localCount > 0) && (!onlyReset)) {

            documentCount++;

            // If classes are supported and the class value is valid, increase
            // the corresponding class count as well
            if (classCount != null)
                if ((classValue >= 0) && (classValue < classCount.length))
                    classCount[classValue]++;

            // Reset the local count
            localCount = 0;
        } else
            localCount = 0;

    }

    /**
     * Return the frequency for the current document.
     * 
     * @return the frequency for the current document
     */
    public int getLocalFrequency() {

        return localCount;
    }

    /**
     * Return the document frequency for this word.
     * 
     * @return the document frequency
     */
    public int getDocumentFrequency() {

        return documentCount;
    }

    /**
     * Set the document frequency
     * 
     * @param v the value
     */
    public void setDocumentFrequency(int v) {

        documentCount = v;
    }

    /**
     * Set the class frequency
     * 
     * @param index the index of the class value
     * @param v the value to set
     */
    public void setClassFrequency(int index, int v) {

        if ((index >= 0) && (index < classCount.length))
            classCount[index] = v;

    }

    /**
     * Return the class frequency of this word for a given class.
     * 
     * @param classValue the class value
     * @return the class frequency or 0,if classes are not supported or the class value is out of range
     */
    public int getClassFrequency(int classValue) {

        // If classes are supported and the class value is valid, return actual
        // value
        if (classCount != null)
            if ((classValue >= 0) && (classValue < classCount.length))
                return classCount[classValue];
            else
                return 0;
        // Otherwise return zero
        else
            return 0;
    }

    /**
     * Returns the word.
     * 
     * @return a String
     */
    public String getWord() {
        return word;
    }

}

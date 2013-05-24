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
package edu.udo.cs.wvtool.generic.stemmer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * An abstract stemming class using the wordnet dicitionary. Subclasses control which word form is derived from the base form. The prerequisite for using any subclass of this class is an installation of WordNet 2.1. Also, you need to provide the parameter -Dwvtool.wnconfig=<wnfile> to your program, where <wnfile> is a configuration file for the JWNL system. An example for such a file can be found in the sample directory. For more information refer to the JWNL project.
 * 
 * http://jwordnet.sourceforge.net
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public abstract class AbstractWordNetStemmer extends AbstractStemmer {

    private final Dictionary dictionary;

    private final SimpleStemmer stemmer;

    private final int maxSenses;

    public AbstractWordNetStemmer(SimpleStemmer stemmer, int maxSenses) {

        super();

        this.maxSenses = maxSenses;
        this.stemmer = stemmer;

        if (!JWNL.isInitialized()) {

            String configFileName = System.getProperty("wvtool.wnconfig");

            // initialize JWNL (this must be done before JWNL can be used)
            try {
                JWNL.initialize(new FileInputStream(configFileName));
            } catch (FileNotFoundException e) {

                WVToolLogger.getGlobalLogger().logException("Could not read JWNL configuration file.", e);

            } catch (JWNLException e) {
                WVToolLogger.getGlobalLogger().logException("Could not initialize JWNL.", e);

            }

        }

        dictionary = Dictionary.getInstance();

    }

    public AbstractWordNetStemmer() {
        this(new LovinsStemmerWrapper(), 1);
    }

    protected IndexWord getIndexWord(String s) throws JWNLException, WVToolException {

        IndexWord word = null;

        POS[] posTags = new POS[] { POS.NOUN, POS.ADJECTIVE, POS.VERB, POS.ADVERB };

        IndexWordSet wordSet = dictionary.lookupAllIndexWords(s);

        if (wordSet != null)
            for (int i = 0; i < posTags.length && (word == null); i++)
                word = wordSet.getIndexWord(posTags[i]);

        if ((word == null) && (stemmer != null)) {

            IndexWordSet stemmedWordSet = dictionary.lookupAllIndexWords(stemmer.getBase(s));
            if (stemmedWordSet != null)
                for (int i = 0; i < posTags.length && (word == null); i++)
                    word = stemmedWordSet.getIndexWord(posTags[i]);
        }

        return word;
    }

    /**
     * Obtain a derived form of the specified word.
     * 
     * @param word a word
     * @return a String representig the derived form
     * @throws JWNLException
     */
    protected abstract String getWordForm(IndexWord word) throws JWNLException;

    public String getBase(String s) throws WVToolException {

        String result = null;
        try {
            IndexWord word = getIndexWord(s);

            if (word != null)
                if (word.getSenseCount() <= maxSenses)
                    result = getWordForm(word);
                else
                    result = word.getLemma();
            else {
                result = s;
            }
        } catch (JWNLException e) {

            result = s;
        }

        return result;
    }

}

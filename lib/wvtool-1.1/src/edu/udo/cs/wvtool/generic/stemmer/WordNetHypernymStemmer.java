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

import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;

/**
 * Replaces a word by a hypernym. See also AbstractWordNetStemmer.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class WordNetHypernymStemmer extends AbstractWordNetStemmer {

    public WordNetHypernymStemmer() {
        super();
    }

    public WordNetHypernymStemmer(SimpleStemmer stemmer, int maxSenses) {
        super(stemmer, maxSenses);
    }

    protected String getWordForm(IndexWord word) {

        if (word.getSenseCount() == 1) {

            PointerTargetNodeList hypernyms;
            try {
                hypernyms = PointerUtils.getInstance().getDirectHypernyms(word.getSense(1));
            } catch (Exception e) {
                hypernyms = null;
            }

            if ((hypernyms != null) && (hypernyms.size() > 0))
                return ((PointerTargetNode) hypernyms.get(0)).getSynset().getWord(0).getLemma();
            else
                return word.getLemma();

        } else
            return word.getLemma();

    }

}

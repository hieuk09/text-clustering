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
package edu.udo.cs.wvtool.generic.vectorcreation;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * Generate word vector by simply using term frequencies. The resulting vector is normalized.
 * 
 * @author Michael Wurst
 * @version $Id: TermFrequency.java,v 1.4 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public class TermFrequency implements WVTVectorCreator {

    /**
     * @see edu.udo.cs.wvtool.generic.vectorcreation.WVTVectorCreator#createVector(int[], int, edu.udo.cs.wvtool.wordlist.WVTWordList, edu.udo.cs.wvtool.main.WVTDocumentInfo)
     */
    public WVTWordVector createVector(int[] frequencies, int numTermOccurences, WVTWordList wordList, WVTDocumentInfo d) {

        int numTerms = wordList.getNumWords();

        // Create the result structure
        WVTWordVector result = new WVTWordVector();
        double[] wv = new double[numTerms];

        // If document contains at least one term
        if (numTermOccurences > 0) {
            // Create the vector
            double length = 0.0;
            for (int i = 0; i < wv.length; i++) {
                wv[i] = ((double) frequencies[i]) / ((double) numTermOccurences);
                length += wv[i] * wv[i];
            }

            length = Math.sqrt(length);

            // Normalize the vector
            if (length > 0.0)
                for (int i = 0; i < wv.length; i++)
                    wv[i] = wv[i] / length;
        } else {
            for (int i = 0; i < wv.length; i++)
                wv[i] = 0.0;
        }
        result.setDocumentInfo(d);
        result.setValues(wv);

        return result;

    }

}

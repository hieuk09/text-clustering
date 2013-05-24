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
 * Create the vector by taking the number of occurences.
 * 
 * @author Michael Wurst
 * @version $Id: BinaryOccurrences.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public class BinaryOccurrences implements WVTVectorCreator {

    public WVTWordVector createVector(

    int[] frequencies, int numTermOccurences, WVTWordList wordList, WVTDocumentInfo d) {

        int numTerms = wordList.getNumWords();

        // Create the result structure
        WVTWordVector result = new WVTWordVector();
        double[] wv = new double[numTerms];

        // Create the vector
        for (int i = 0; i < wv.length; i++)
            if (frequencies[i] > 0)
                wv[i] = 1;
            else
                wv[i] = 0;

        result.setDocumentInfo(d);
        result.setValues(wv);

        return result;

    }

}

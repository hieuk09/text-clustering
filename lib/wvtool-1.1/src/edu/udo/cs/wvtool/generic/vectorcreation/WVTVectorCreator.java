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
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * This interface represents a mechanism by which an individual word vector is created.
 * 
 * @author Michael Wurst
 * @version $Id: WVTVectorCreator.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public interface WVTVectorCreator {

    /**
     * Create a word vector from term frequencies and a word list.
     * 
     * @param frequencies an array containing the frequencies, by which individual terms occur in the document
     * @param wordList a pointer to a <code>WVTWordList</code>
     * @param d the <code>WVTDocumentInfo</code> value, describing the document being processed
     * @return a <code>WVTWordVector</code> value
     * @exception Exception if an error occurs
     */
    public WVTWordVector createVector(int[] frequencies, int numTermOccurences, WVTWordList wordList, WVTDocumentInfo d) throws WVToolException;

}

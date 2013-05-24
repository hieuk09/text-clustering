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
package edu.udo.cs.wvtool.generic.inputfilter;

import java.io.InputStream;
import java.io.Reader;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * This interface represents a mechanism, which converts content given by an input stream to a stream of characters. (e.g. a PDF file to plain text). In most cases, the intention is to get plain natural text from this processing step.
 * 
 * @author Michael Wurst
 * @version $Id: WVTInputFilter.java,v 1.3 2007/05/20 18:06:04 mjwurst Exp $
 * 
 */
public interface WVTInputFilter {

    /**
     * Convert the input stream to plain natural text.
     * 
     * @param source the <code>InputStream</code> to read from
     * @param d the <code>WVTDocumentInfo</code> about the document
     * @return a <code>Reader</code>
     * @exception Exception if an error occurs
     */
    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) throws WVToolException;

}

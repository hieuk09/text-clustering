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
package edu.udo.cs.wvtool.generic.tokenizer;

import java.io.Reader;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * Interface, which represents a mechanism to convert a stream of characters to a stream of tokens, deleting all seperators.
 * 
 * @author Michael Wurst
 * @version $Id: WVTTokenizer.java,v 1.3 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */
public interface WVTTokenizer {

    /**
     * Tokenize a character stream.
     * 
     * @param source the <code>Reader</code> from which to get the character stream
     * @param d the <code>WVTDocumentInfo</code> value, describing the document being processed
     * @return a <code>TokenEnumeration</code>
     * @exception Exception if an error occurs
     */
    public TokenEnumeration tokenize(Reader source, WVTDocumentInfo d) throws WVToolException;

}

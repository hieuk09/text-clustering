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
package edu.udo.cs.wvtool.generic.loader;

import java.io.InputStream;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * This interface represents a mechanism by which a document is loaded. Loading refers to the operation of opening a stream to the source of the document.
 * 
 * @author Michael Wurst
 * @version $Id: WVTDocumentLoader.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public interface WVTDocumentLoader {

    /**
     * Open the document and return an input stream on it.
     * 
     * @param d the <code>WVTDocumentInfo</code> about the document being loaded
     * @return an <code>InputStream</code>
     * @exception Exception if an error occurs
     */
    public InputStream loadDocument(WVTDocumentInfo d) throws WVToolException;

    /**
     * Close the resource from which the given document has been read.
     * 
     * @param d the <code>WVTDocumentInfo</code> about the document
     */
    public void close(WVTDocumentInfo d) throws WVToolException;

}

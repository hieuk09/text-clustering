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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolIOException;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Read XML Input ignoring the tags. The encoding is determined using information from the document info and not from the xml document itself.
 * 
 * @author Michael Wurst
 * @version $Id: XMLInputFilter.java,v 1.4 2007/05/20 18:06:04 mjwurst Exp $
 * 
 */
public class XMLInputFilter implements WVTInputFilter {

    /**
     * @see edu.udo.cs.wvtool.generic.inputfilter.WVTInputFilter#convertToPlainText(InputStream, WVTDocumentInfo)
     */
    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) throws WVToolIOException {

        Reader reader = null;
        try {
            reader = new InputStreamReader(source, d.getContentEncoding());
        } catch (UnsupportedEncodingException e) {
            reader = null;
            WVToolLogger.getGlobalLogger().logMessage("Warning: Encoding " + d.getContentEncoding() + " unknown. Using default.", WVToolLogger.WARNING);
        }

        if (reader == null) {
            reader = new InputStreamReader(source);
        }

        try {
            return new TagIgnoringReader(reader);
        } catch (IOException e) {
            WVToolLogger.getGlobalLogger().logException("Could not read stream", e);
            throw new WVToolIOException("TagIgnoringReader could not read the source:", e);
        }
    }

}

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Very simple tag ignoring reader. The encoding is determined using information from the document info and not from the xml document itself.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class SimpleTagIgnoringReader implements WVTInputFilter {

    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) throws WVToolException {

        // Read text into String

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

        StringBuffer textBuf = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(reader);
            String buf = null;

            while ((buf = in.readLine()) != null) {
                textBuf.append(buf);
            }

            in.close();
        } catch (IOException e) {
            throw new WVToolException("Could not read document " + d.getSourceName());
        }

        // Delete all tags

        int pos1 = 0;
        int pos2 = 0;

        do {
            pos1 = textBuf.indexOf("<");
            pos2 = textBuf.indexOf(">", pos1);

            if ((pos1 >= 0) && (pos2 > pos1))
                textBuf.delete(pos1, pos2 + 1);
        } while ((pos1 >= 0) && (pos2 > pos1));

        return new StringReader(textBuf.toString() + " ");
    }

}

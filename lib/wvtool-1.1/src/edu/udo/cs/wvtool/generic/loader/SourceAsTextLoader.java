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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolIOException;

/**
 * This loader simply uses the defined source as text. It is a sort of hack in order to allow reading text directly from string attributes of Yale.
 * 
 * @author Ingo Mierswa, Michael Wurst
 * @version $Id: SourceAsTextLoader.java,v 1.5 2007/05/22 16:38:58 mjwurst Exp $
 */
public class SourceAsTextLoader implements WVTDocumentLoader {

    private InputStream stream;

    /** Open the document and return an input stream on it. */
    public InputStream loadDocument(WVTDocumentInfo d) {

        byte[] bArray = d.getSourceName().getBytes();
        stream = new ByteArrayInputStream(bArray);

        return stream;
    }

    /** Close the resource from which the given document has been read. */
    public void close(WVTDocumentInfo d) throws WVToolException {
        try {
            stream.close();
        } catch (IOException e) {
            throw new WVToolIOException("Could not close stream", e);
        }
    }

}

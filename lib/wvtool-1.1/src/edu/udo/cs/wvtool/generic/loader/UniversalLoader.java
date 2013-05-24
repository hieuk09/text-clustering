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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolIOException;

/**
 * Loader, which is able to load individual documents from an URL or simply from a local file given by the source name in document information. The loader first tries to parse the given document source as a URL and only if this fails, tries to interpret it as a local path.
 * 
 * @author Michael Wurst
 * @version $Id: UniversalLoader.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public class UniversalLoader implements WVTDocumentLoader {

    private InputStream currentInputStream = null;

    /**
     * @see edu.udo.cs.wvtool.generic.loader.WVTDocumentLoader#loadDocument(WVTDocumentInfo)
     */
    public InputStream loadDocument(WVTDocumentInfo d) throws WVToolException {

        try {

            // Determine whether URL or local path

            // Try to parse the document source as URL

            URL sourceUrl = null;
            boolean validUrlFormat = true;

            try {
                sourceUrl = new URL(d.getSourceName());
            } catch (MalformedURLException e) {
                validUrlFormat = false;
            }

            // If in URL format, open a connection
            if (validUrlFormat) {

                URLConnection connection = sourceUrl.openConnection();
                return connection.getInputStream();

            }

            // If not, try to open as a local file
            else {

                InputStream is = new FileInputStream(d.getSourceName());
                currentInputStream = is;
                return is;
            }
        } catch (Exception e) {
            throw new WVToolIOException("Could not open stream.", e);
        }
    }

    /**
     * @see edu.udo.cs.wvtool.generic.loader.WVTDocumentLoader#close(WVTDocumentInfo)
     */
    public void close(WVTDocumentInfo d) throws WVToolException {

        try {
            if (currentInputStream != null)
                currentInputStream.close();
        } catch (IOException e) {

            throw new WVToolIOException("Could not close strean.", e);
        }

    }

}
